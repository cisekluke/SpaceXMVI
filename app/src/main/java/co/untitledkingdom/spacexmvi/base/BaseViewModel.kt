package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S, *>, A : BaseMviAction<S>> :
    ViewModel() {
    
    private lateinit var view: V

    private val compositeDisposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    private var subscribed = false
    private var isInitialized = false

    protected abstract val defaultViewState: S

    protected abstract fun <I : BaseMviIntent> intentToAction(intent: I): Observable<A>

    // TODO is this should be called navigation for sure
    protected open fun <I : BaseMviIntent> intentToNavigation(intent: I) {}

    // TODO some cleanup with those methods
    internal fun bind() {
        // TODO this check looks awful
        if (!subscribed) navigation()
        saveState(mapIntents())
    }

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    internal fun attachView(view: V) {
        this.view = view
        isInitialized = true
    }

    internal fun unsubscribe() {
        subscribed = false
    }

    protected fun just(action: A): Observable<A> = Observable.just(action)

    private fun saveState(intents: Observable<A>) {
        if (!subscribed) {
            intents.scan(getViewState(), this::reduce)
                .replay(1)
                .autoConnect(0)
                .subscribe(stateSubject)

            subscribed = true
        }

        renderStates()
    }

    private fun navigation() {
        compositeDisposable.add(view.emitNavigationIntent().subscribe { intentToNavigation(it) })
    }

    @MainThread
    private fun renderStates() {
        compositeDisposable.add(
            stateSubject.distinctUntilChanged()
                .subscribe { state ->
                    view.render(state)
                }
        )
    }

    private fun mapIntents(): Observable<A> = view.emitIntent()
        .flatMap { intentToAction(it) }

    fun getViewState(): S = stateSubject.value ?: defaultViewState

    fun setInitialViewState(viewState: S) {
        stateSubject.onNext(viewState)
    }

    fun isAlreadyInitialized() = isInitialized

    private fun reduce(previousState: S, partialState: A): S = partialState.reduce(previousState)
}