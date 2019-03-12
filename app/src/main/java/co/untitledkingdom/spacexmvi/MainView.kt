package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseMviView
import io.reactivex.Observable

interface MainView : BaseMviView<MainViewState> {

    fun emitButtonClick(): Observable<Boolean>

    fun emitClearButton(): Observable<Boolean>

    override fun render(viewState: MainViewState)
}