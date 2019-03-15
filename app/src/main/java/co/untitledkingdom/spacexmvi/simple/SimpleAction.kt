package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviAction

sealed class SimpleAction : BaseMviAction<SimpleViewState> {

    object Nothing : SimpleAction() {
        override fun reduce(previousState: SimpleViewState): SimpleViewState {
            return SimpleViewState()
        }
    }

    data class DisplayOutput(val input: String) : SimpleAction() {
        override fun reduce(previousState: SimpleViewState): SimpleViewState {
            return SimpleViewState(input)
        }
    }
}