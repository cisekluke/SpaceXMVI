package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S, *>, A : BaseMviAction<S>> :
    ViewModel() {

    private lateinit var view: V

    private val compositeDisposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    private var subscribed = false

    protected abstract val defaultViewState: S

    protected abstract fun <I : BaseMviIntent> intentToAction(intent: I): Observable<A>

    protected open fun <I : BaseMviIntent> intentToNavigation(intent: I) {}

    internal fun bind() {
        if (!subscribed) navigation()
        saveState(mapIntents())
    }

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    internal fun attachView(view: V) {
        this.view = view
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
                .subscribe { state -> view.render(state) }
        )
    }

    private fun mapIntents(): Observable<A> = view.emitIntent()
        .flatMap { intentToAction(it) }

    private fun getViewState(): S = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: A): S = partialState.reduce(previousState)
}