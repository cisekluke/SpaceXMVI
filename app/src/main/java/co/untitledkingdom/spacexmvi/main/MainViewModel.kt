package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseViewModel

class MainViewModel(private val mainInteractor: MainInteractor = MainInteractor()) :
    BaseViewModel<MainViewState, MainView, PartialMainViewState>() {

    override val defaultViewState: MainViewState
        get() = MainViewState()

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

        render(intents = mergedIntentsObservable)
    }
}