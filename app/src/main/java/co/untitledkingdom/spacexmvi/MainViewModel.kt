package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseViewModel
import io.reactivex.Observable

class MainViewModel(private val mainInteractor: MainInteractor = MainInteractor()) :
    BaseViewModel<MainViewState, MainView, MainAction>() {

    override fun bind() {
        render(intents = view().emitIntents().flatMap { intentsToAction(it) }, defaultViewState = MainViewState())
    }

    private fun intentsToAction(intent: MainIntent): Observable<MainAction> =
        when (intent) {
            is MainIntent.FetchRocketsState -> mainInteractor.fetchRocketList().startWith(MainAction.ShowProgress)
            is MainIntent.ClearState -> Observable.just(MainAction.ClearStates)
        }
}