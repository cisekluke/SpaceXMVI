package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.MvvmiAction
import co.untitledkingdom.spacexmvi.models.Rocket

sealed class MainAction : MvvmiAction<MainViewState> {

    object ShowProgress : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState(progress = true)
    }

    object DisplayError : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState(error = true)
    }

    class ListFetched(private val rocketList: List<Rocket>) : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState(rocketList = rocketList)
    }

    object ClearStates : MainAction() {
        override fun reduce(previousState: MainViewState) =
            MainViewState()
    }

    data class DisplayFragment(private val displayFragment: Boolean = false) : MainAction() {
        override fun reduce(previousState: MainViewState): MainViewState =
            previousState.copy(displayFragment = displayFragment)
    }

    object Nothing : MainAction() {
        override fun reduce(previousState: MainViewState): MainViewState =
            MainViewState()
    }
}