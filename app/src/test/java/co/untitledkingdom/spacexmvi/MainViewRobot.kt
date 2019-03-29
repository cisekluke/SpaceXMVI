package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.main.MainMviPresenter
import co.untitledkingdom.spacexmvi.main.MainView
import co.untitledkingdom.spacexmvi.main.MainViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MainViewRobot(mainViewModel: MainMviPresenter) : BaseMviViewRobot<MainView, MainMviPresenter, MainViewState>(mainViewModel) {

    private val buttonClickSubject = PublishSubject.create<Boolean>()
    private val clearClickSubject = PublishSubject.create<Boolean>()
    private val fragmentClickSubject = PublishSubject.create<Boolean>()

    override val renderedStates = arrayListOf<MainViewState>()

    override val view = object : MainView {
        override fun emitFragmentClick(): Observable<Boolean> = fragmentClickSubject

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

    fun emitFragmentClick() {
        fragmentClickSubject.onNext(true)
    }
}