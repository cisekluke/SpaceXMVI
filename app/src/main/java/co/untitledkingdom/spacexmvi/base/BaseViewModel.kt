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

    abstract fun bind()

    internal fun attachView(mView: V) {
        view = mView
    }

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    protected fun view(): V = view

    protected fun render(intents: Observable<P>, defaultViewState: S) {
        intents.scan(getViewState(defaultViewState), this::reduce)
            .replay(1)
            .autoConnect(0)
            .subscribe(stateSubject)
        renderStates()
    }

    @MainThread
    private fun renderStates() {
        compositeDisposable.add(
            stateSubject.distinctUntilChanged()
                .subscribe { state -> view.render(state) })
    }

    private fun getViewState(defaultViewState: S) = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: P): S = partialState.reduce(previousState)
}