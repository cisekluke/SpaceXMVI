package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseMviView
import io.reactivex.Observable

interface MainView : BaseMviView<MainViewState> {

    fun emitIntents(): Observable<MainIntent>
}