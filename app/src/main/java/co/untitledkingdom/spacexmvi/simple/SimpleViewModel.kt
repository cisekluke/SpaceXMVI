package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviIntent
import co.untitledkingdom.spacexmvi.base.BaseViewModel
import io.reactivex.Observable

class SimpleViewModel : BaseViewModel<SimpleViewState, SimpleView, SimpleAction>() {

    override val defaultViewState: SimpleViewState
        get() = SimpleViewState()

    override fun <I : BaseMviIntent> intentToAction(intent: I): Observable<SimpleAction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
