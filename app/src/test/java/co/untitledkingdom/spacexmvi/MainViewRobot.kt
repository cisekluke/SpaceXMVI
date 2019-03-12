package co.untitledkingdom.spacexmvi

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.assertEquals

class MainViewRobot(mainViewModel: MainViewModel) : BaseMviViewRobot<MainView, MainViewState, MainViewModel>() {

    private val buttonClickSubject = PublishSubject.create<Boolean>()
    private val clearClickSubject = PublishSubject.create<Boolean>()

    override val renderedStates = arrayListOf<MainViewState>()
    override val viewModel = mainViewModel
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