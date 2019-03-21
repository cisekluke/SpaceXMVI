package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseMviPresenter

class MainMviPresenter(private val mainInteractor: MainInteractor = MainInteractor()) :
    BaseMviPresenter<MainViewState, MainView, PartialMainViewState>() {

    override fun bind() {
        val buttonClickObservable =
            view().emitButtonClick()
                .flatMap {
                    mainInteractor.fetchRocketList().startWith(PartialMainViewState.ProgressState)
                }

        val clearButtonObservable =
            view().emitClearButton()
                .map<PartialMainViewState> {
                    PartialMainViewState.ClearPreviousStates
                }

        val mergedIntentsObservable = mergeStates(buttonClickObservable, clearButtonObservable)

        render(intents = mergedIntentsObservable, defaultViewState = MainViewState())
    }
}