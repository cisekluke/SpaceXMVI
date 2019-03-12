package co.untitledkingdom.spacexmvi

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MainViewRobot(mainViewModel: MainViewModel) : BaseMviViewRobot<MainView, MainViewModel, MainViewState>(mainViewModel) {

    private val buttonClickSubject = PublishSubject.create<Boolean>()
    private val clearClickSubject = PublishSubject.create<Boolean>()

    override val renderedStates = arrayListOf<MainViewState>()
    override val mainView = object : MainView {
        override fun emitClearButton(): Observable<Boolean> = clearClickSubject

        override fun emitButtonClick(): Observable<Boolean> = buttonClickSubject

        override fun render(mainViewState: MainViewState) {
            renderedStates.add(mainViewState)
        }
    }

    fun emitButtonClick() {
        buttonClickSubject.onNext(true)
    }

    fun clearButtonClick() {
        clearClickSubject.onNext(true)
    }
}