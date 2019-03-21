package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviPresenter

class SimplePresenter : BaseMviPresenter<SimpleViewState, SimpleView, PartialSimpleViewState>() {

    override fun bind() {
        val buttonClickObservable = view().emitUpdateButtonClick()
            .map<PartialSimpleViewState> { PartialSimpleViewState.DisplayOutput(it) }

        render(buttonClickObservable, SimpleViewState())
    }
}