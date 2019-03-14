package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S, *>, A : BaseMviAction<S>> :
    ViewModel() {

    private lateinit var view: V

    private val compositeDisposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<S>()
    private val stopSubject = PublishSubject.create<Any>()

    protected abstract val defaultViewState: S

    protected abstract fun <I : BaseMviIntent> intentToAction(intent: I): Observable<A>

    internal fun bind() {
        render(intents = mapIntents())
    }

    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
        stopSubject.onNext(true)
    }

    internal fun attachView(view: V) {
        this.view = view
    }

    protected fun just(intent: A): Observable<A> = Observable.just(intent)

    private fun render(intents: Observable<A>) {
        intents.scan(getViewState(), this::reduce)
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

    private fun mapIntents() = view.emitIntent()
        .takeUntil(stopSubject)
        .flatMap { intentToAction(it) }

    private fun getViewState() = stateSubject.value ?: defaultViewState

    private fun reduce(previousState: S, partialState: A): S = partialState.reduce(previousState)
}