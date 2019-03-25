package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseMviPresenter

class MainMviPresenter(private val mainInteractor: MainInteractor = MainInteractor()) :
    BaseMviPresenter<MainViewState, MainView, PartialMainViewState>() {

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

        val fragmentButtonObservable =
            view().emitFragmentClick()
                .map<PartialMainViewState> { PartialMainViewState.OpenFragmentState(it) }

        val mergedIntentsObservable = mergeStates(buttonClickObservable, clearButtonObservable, fragmentButtonObservable)

        render(intents = mergedIntentsObservable)
    }
}