package co.untitledkingdom.spacexmvi

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MainViewRobot(mainViewModel: MainViewModel) : BaseMviViewRobot<MainView, MainViewModel, MainViewState>(mainViewModel) {

    /** typical subjects to emit simulated actions */
    private val buttonClickSubject = PublishSubject.create<Boolean>()
    private val clearClickSubject = PublishSubject.create<Boolean>()

    /** override array were you will be storing your rendered states */
    override val renderedStates = arrayListOf<MainViewState>()

    /** override view which will be attached to view model */
    override val mainView = object : MainView {
        override fun emitClearButton(): Observable<Boolean> = clearClickSubject

        override fun emitButtonClick(): Observable<Boolean> = buttonClickSubject

        override fun render(mainViewState: MainViewState) {
            /** add states to previously overridden states holder */
            renderedStates.add(mainViewState)
        }
    }

    /** public methods to simulate actions */
    fun emitButtonClick() {
        buttonClickSubject.onNext(true)
    }

    fun clearButtonClick() {
        clearClickSubject.onNext(true)
    }
}