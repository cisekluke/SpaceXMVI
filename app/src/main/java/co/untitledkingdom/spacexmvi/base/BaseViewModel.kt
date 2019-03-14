package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S, *>, P : BaseMviPartialState<S>> :
    ViewModel() {

    private lateinit var view: V

    private val compositeDisposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    protected abstract val defaultViewState: S

    internal fun bind() {
        render(intents = mapIntents())
    }

    internal fun attachView(mView: V) {
        view = mView
    }

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    protected fun view(): V = view

    private fun render(intents: Observable<P>) {
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

    protected abstract fun <I : BaseMviIntent> intentToAction(intent: I): Observable<P>

    private fun mapIntents() = view.emitIntent().flatMap { intentToAction(it) }

    private fun getViewState(defaultViewState: S) = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: P): S = partialState.reduce(previousState)
}