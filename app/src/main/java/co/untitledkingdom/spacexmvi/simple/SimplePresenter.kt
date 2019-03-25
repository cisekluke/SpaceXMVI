package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviPresenter

class SimplePresenter : BaseMviPresenter<SimpleViewState, SimpleView, PartialSimpleViewState>() {

    override val defaultViewState: SimpleViewState
        get() = SimpleViewState()

    override fun bind() {
        val buttonClickObservable = view().emitUpdateButtonClick()
            .map<PartialSimpleViewState> { PartialSimpleViewState.DisplayOutput(it) }

        render(buttonClickObservable)
    }
}