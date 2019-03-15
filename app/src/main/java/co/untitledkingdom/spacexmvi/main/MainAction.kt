package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseMviAction
import co.untitledkingdom.spacexmvi.models.Rocket

sealed class MainAction : BaseMviAction<MainViewState> {

    object ShowProgress : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState(progress = true)
    }

    object ErrorState : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState(error = true)
    }

    class ListFetchedState(private val rocketList: List<Rocket>) : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState(rocketList = rocketList)
    }

    object ClearStates : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState()
    }

    object Nothing : MainAction() {
        override fun reduce(previousState: MainViewState): MainViewState =
            MainViewState()
    }
}