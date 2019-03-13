package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseViewModel

class MainViewModel(private val mainInteractor: MainInteractor = MainInteractor()) :
    BaseViewModel<MainViewState, MainView, PartialMainViewState>() {

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

        saveStates(intents = mergedIntentsObservable, defaultViewState = MainViewState())
        renderStates()
    }
}