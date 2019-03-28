package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S, *>, A : BaseMviAction<S>> :
    ViewModel() {

    protected abstract val defaultViewState: S

    private lateinit var view: V

    private val compositeDisposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    private var subscribed = false
    private var isInitialized = false

    fun isAlreadyInitialized() = isInitialized

    fun setInitialViewState(viewState: S) {
        stateSubject.onNext(viewState)
    }

    internal fun attachView(view: V) {
        this.view = view
        isInitialized = true
    }

    internal fun bind() {
        if (!subscribed) {
            subscribeStates(mapIntents())
            subscribeWithoutStates()
        }

        renderStates()
    }

    private fun subscribeStates(intents: Observable<A>) {
        intents.scan(getViewState(), this::reduce)
            .replay(1)
            .autoConnect(0)
            .subscribe(stateSubject)

        subscribed = true
    }

    fun getViewState(): S = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: A): S =
        partialState.reduce(previousState)

    private fun mapIntents(): Observable<A> =
        view.emitIntent()
            .flatMap { intentToAction(it) }

    protected abstract fun <I : BaseMviIntent> intentToAction(intent: I): Observable<A>

    private fun subscribeWithoutStates() {
        compositeDisposable.add(
            view.emitNavigationIntent()
                .subscribe { intentWithoutAction(it) }
        )
    }

    protected open fun <I : BaseMviIntent> intentWithoutAction(intent: I) {}

    @MainThread
    private fun renderStates() {
        compositeDisposable.add(
            stateSubject.distinctUntilChanged()
                .subscribe { state ->
                    view.render(state)
                }
        )
    }

    protected fun just(action: A): Observable<A> = Observable.just(action)

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    internal fun unsubscribe() {
        subscribed = false
    }
}