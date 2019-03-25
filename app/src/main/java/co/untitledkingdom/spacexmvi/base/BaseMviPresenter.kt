package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseMviPresenter<S : BaseMviViewState, V : BaseMviView<S>, P : BaseMviPartialState<S>> {

    private lateinit var view: V

    private val disposables = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    private var initialized = false
    abstract val defaultViewState: S

    abstract fun bind()

    internal fun attachView(view: V) {
        this.view = view
    }

    @CallSuper
    internal open fun unbind() {
        disposables.clear()
    }

    internal fun deinitialize() {
        initialized = false
    }

    protected fun view(): V = view

    protected fun mergeStates(vararg states: Observable<P>): Observable<P> =
        Observable.merge(states.asIterable())

    protected fun render(intents: Observable<P>) {
        if (!initialized) {
            intents.scan(getViewState(), this::reduce)
                .subscribe(stateSubject)
            initialized = true
        }
        renderStates()
    }

    fun saveLastViewState(key: String, outState: Bundle) {
        outState.putParcelable(key, stateSubject.value)
    }

    fun initState(viewState: S?) {
        stateSubject.onNext(viewState ?: defaultViewState)
    }

    @MainThread
    private fun renderStates() {
        disposables.add(
            stateSubject.distinctUntilChanged()
                .subscribe { state ->
                    view.render(state)
                })
    }

    private fun getViewState() = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: P): S = partialState.reduce(previousState)
}