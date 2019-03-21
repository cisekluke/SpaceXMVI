package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviView
import io.reactivex.Observable

interface SimpleView : BaseMviView<SimpleViewState> {

    fun emitUpdateButtonClick(): Observable<String>
}