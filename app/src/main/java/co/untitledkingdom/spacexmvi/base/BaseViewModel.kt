package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S>, P : BaseMviPartialState<S>> :
    ViewModel() {

    private lateinit var view: V

    private val compositeDisposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    private var subscribed = false
    private var isInitialized = false

    protected abstract val defaultViewState: S

    abstract fun bind()

    internal fun attachView(view: V) {
        this.view = view
        isInitialized = true
    }

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    internal fun unsubscribe() {
        subscribed = false
    }

    protected fun view(): V = view

    protected fun mergeStates(vararg states: Observable<P>): Observable<P> =
        Observable.merge(states.asIterable())

    protected fun render(intents: Observable<P>) {
        if (!subscribed) {
            intents.scan(getViewState(), this::reduce)
               .subscribe(stateSubject)
            subscribed = true
        }
        renderStates()
    }

    @MainThread
    private fun renderStates() {
        compositeDisposable.add(
            stateSubject.distinctUntilChanged()
                .subscribe { state -> view.render(state) })
    }

    fun setInitialViewState(viewState: S) {
        stateSubject.onNext(viewState)
    }

    fun isAlreadyInitialized() = isInitialized

    fun getViewState() = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: P): S = partialState.reduce(previousState)
}