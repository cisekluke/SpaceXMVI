package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
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

    open fun unbind() {
        compositeDisposable.clear()
    }

    protected fun view(): V = view

    protected fun mergeStates(vararg expectedStates: Observable<P>): Observable<P> =
            Observable.merge(expectedStates.asIterable())

    protected fun saveState(intents: Observable<P>, defaultViewState: S) {
        intents.scan(getViewState(defaultViewState), this::reduce)
                .subscribe(stateSubject)
    }

    protected fun renderState() {
        compositeDisposable.add(stateSubject.distinctUntilChanged().subscribe { state -> view.render(state) })
    }

    private fun getViewState(defaultViewState: S) = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: P): S = partialState.reduce(previousState)
}