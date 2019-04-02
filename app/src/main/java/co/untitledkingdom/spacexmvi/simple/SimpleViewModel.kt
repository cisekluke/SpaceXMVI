package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.MvvmiIntent
import co.untitledkingdom.spacexmvi.base.MvvmiModel
import io.reactivex.Observable

class SimpleViewModel : MvvmiModel<SimpleViewState, SimpleView, SimpleAction>() {

    override val defaultViewState: SimpleViewState
        get() = SimpleViewState()

    override fun <I : MvvmiIntent> intentToAction(intent: I): Observable<SimpleAction> {
        return when (intent) {
            is SimpleIntent.ShowOutputStage -> just(SimpleAction.DisplayOutput(intent.input))
            else -> just(SimpleAction.Nothing)
        }
    }


}
