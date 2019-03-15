package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.main.MainIntent
import co.untitledkingdom.spacexmvi.main.MainView
import co.untitledkingdom.spacexmvi.main.MainViewModel
import co.untitledkingdom.spacexmvi.main.MainViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MainViewRobot(mainViewModel: MainViewModel) : BaseMviViewRobot<MainView, MainViewModel, MainViewState>(mainViewModel) {

    private val buttonClickSubject = PublishSubject.create<MainIntent>()
    private val clearClickSubject = PublishSubject.create<MainIntent>()

    override val renderedStates = arrayListOf<MainViewState>()

    override val view = object : MainView {

        override fun emitIntent(): Observable<MainIntent> =
            Observable.merge(buttonClickSubject, clearClickSubject)

        override fun render(viewState: MainViewState) {
            renderedStates.add(viewState)
        }
    }

    fun emitButtonClick() {
        buttonClickSubject.onNext(MainIntent.FetchRocketsState)
    }

    fun clearButtonClick() {
        clearClickSubject.onNext(MainIntent.ClearState)
    }
}