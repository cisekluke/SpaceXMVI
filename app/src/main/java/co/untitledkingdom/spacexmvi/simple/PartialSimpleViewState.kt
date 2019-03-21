package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviPartialState

sealed class PartialSimpleViewState : BaseMviPartialState<SimpleViewState> {

    data class DisplayOutput(val input: String) : PartialSimpleViewState() {
        override fun reduce(previousState: SimpleViewState): SimpleViewState {
            return SimpleViewState(input)
        }
    }
}