package co.untitledkingdom.spacexmvi.base

import io.reactivex.Observable

interface BaseMviView<in V : BaseMviViewState, I : BaseMviIntent> {

    fun render(viewState: V)

    fun emitIntent(): Observable<I>

    fun emitIntentWithoutAction(): Observable<I> = Observable.never<I>()
}