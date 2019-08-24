package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Base class to implement for ViewModels to follow our MVVMI architecture approach. This one
 * is responsible to act as the middleman between view and model.
 *
 * @param S stands for our ViewState data class
 * @param V is our View interface
 * @param A sealed class that holds actions
 */
abstract class BaseViewModel<S : BaseMviViewState, V : BaseMviView<S, *>, A : BaseMviAction<S>> :
    ViewModel() {

    /**
     * It needs to be overridden due to first state render.
     *
     * @return ViewState that should be displayed at first open
     */
    protected abstract val defaultViewState: S

    private lateinit var view: V

    /**
     * Subscription holder that needs to be managed by #bind and #unbind due to lifecycle
     * changes.
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     * Subject that holds states and remembers last state.
     *
     * @return #cold subject that holds state
     */
    private val stateSubject = BehaviorSubject.create<S>()

    /**
     * Internal flag used to know if we have already subscribed or should subscribe to state
     * subject.
     */
    private var subscribed = false
    private var isInitialized = false

    /**
     * Method used to know if it's new instance of ViewModel. It's being used to inform
     * View if state instance should be restored.
     *
     * @return Boolean says if new ViewModel has been created
     */
    internal fun isAlreadyInitialized() = isInitialized

    /**
     * If state has been restored by View this method will set initial value to our subject.
     *
     * @param viewState initial state being loaded from the Bundle
     */
    internal fun setInitialViewState(viewState: S) {
        stateSubject.onNext(viewState)
    }

    /**
     * Happens once during ViewModel life, at the beginning so here we mark it as created
     * and initialized.
     *
     * @param view Setting View.
     */
    internal fun attachView(view: V) {
        this.view = view
        isInitialized = true
    }

    /**
     * This is being called in activity's onStart. If there was no subscription, it will
     * call proper methods. Otherwise it will just call render.
     */
    internal fun bind() {
        if (!subscribed) {
            subscribeStates(Observable.merge(mapIntents(), actionWithoutIntent()))
            subscribeWithoutStates()
        }

        renderStates()
    }

    /**
     * Intents observer is being subscribed to state subject. Important here is to not duplicate
     * subscription because it will cause emitting multiple intents.
     *
     * @param intents intents coming from View
     */
    private fun subscribeStates(intents: Observable<A>) {
        intents.scan(getViewState(), this::reduce)
            .replay(1)
            .autoConnect(0)
            .subscribe(stateSubject)

        subscribed = true
    }

    /**
     * Get value from subject. It returns value or if there's no value then, will use default one
     * provided inside feature ViewModel.
     *
     * @return last view state
     */
    internal fun getViewState(): S = stateSubject.value ?: defaultViewState

    /**
     * Merging out last state with the new one. This one reduces amount of newly created ViewStates
     * usually by coping previous one.
     *
     * @param previousState last emitted state
     * @param partialState new incoming state
     * @return reduced, merged ViewState
     */
    private fun reduce(previousState: S, partialState: A): S =
        partialState.reduce(previousState)

    /**
     * Transforming incoming intent from View to Action.
     *
     * @return Observable that emits Actions
     */
    private fun mapIntents(): Observable<A> =
        view.emitIntent()
            .flatMap { intentToAction(it) }

    /**
     * Override this method to assign which action should be perform due to incoming event.
     * Here you should call Model, Repository or any business layer object if needed.
     *
     * @param intent users interaction coming from the View
     * @return Observable that emits Actions
     */
    protected open fun <I : BaseMviIntent> intentToAction(intent: I): Observable<A> =
        PublishSubject.create()

    /**
     * Override this method to transfer incoming actions from any business logic to the View
     * when they not required any interaction(intent) from UI interface
     */
    protected open fun actionWithoutIntent() : Observable<A> {
        return PublishSubject.create()
    }

    /**
     * Adding custom subscriptions to our Disposable to be confident that customs are being cleared
     * at the same time as intents with action.
     */
    private fun subscribeWithoutStates() {
        compositeDisposable.add(
            view.emitIntentWithoutAction()
                .subscribe { intentWithoutAction(it) }
        )
    }

    /**
     * Override this when you need to create custom subscription that shouldn't return Action,
     * like for example Navigation intent.
     * Here you should call Model, Repository or any business layer object if needed.
     *
     * @param intent users interaction coming from the View
     */
    protected open fun <I : BaseMviIntent> intentWithoutAction(intent: I) {}

    /**
     * Pushing received states to the View, to perform UI action due to received intent and
     * processed action.
     */
    @MainThread
    private fun renderStates() {
        compositeDisposable.add(
            stateSubject.distinctUntilChanged()
                .subscribe { state ->
                    view.render(state)
                }
        )
    }

    /**
     * This one is to simplify creating Observable for simple intents that requires only Action
     * without any additional Model layer calls.
     *
     * @param action our Action that needs to be transformed
     * @return given Action transformed into Observable
     */
    protected fun just(action: A): Observable<A> = Observable.just(action)

    /**
     * Clears subscriptions to avoid memory leaks. This one will be called in onStop.
     * It's open to override but has @CallSuper annotation to be sure that internal subscription
     * were cleared.
     */
    @CallSuper
    internal open fun unbind() {
        compositeDisposable.clear()
    }

    /**
     * This one is being used because ViewModel survives orientation changes but View doesn't
     * so it needs to resubscribe.
     */
    internal fun unsubscribe() {
        subscribed = false
    }
}