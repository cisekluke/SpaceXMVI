package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseMviPartialState
import co.untitledkingdom.spacexmvi.models.Rocket

sealed class PartialMainViewState : BaseMviPartialState<MainViewState> {

    object ProgressState : PartialMainViewState() {
        /** */
        override fun reduce(previousState: MainViewState) =
            MainViewState(progress = true)
    }

    object ErrorState : PartialMainViewState() {
        /** */
        override fun reduce(previousState: MainViewState) =
            MainViewState(error = true)
    }

    class ListFetchedState(private val rocketList: List<Rocket>) : PartialMainViewState() {
        /** */
        override fun reduce(previousState: MainViewState) =
            MainViewState(rocketList = rocketList)
    }

    object ClearPreviousStates : PartialMainViewState() {
        /** */
        override fun reduce(previousState: MainViewState) =
            MainViewState()
    }

    class OpenFragmentState(private val display: Boolean) : PartialMainViewState() {
        override fun reduce(previousState: MainViewState): MainViewState =
            MainViewState(displayFragment = display)
    }
}