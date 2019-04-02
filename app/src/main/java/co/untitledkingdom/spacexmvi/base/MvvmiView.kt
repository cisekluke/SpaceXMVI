package co.untitledkingdom.spacexmvi.base

import io.reactivex.Observable

interface MvvmiView<in V : MvvmiViewState, I : MvvmiIntent> {

    fun render(viewState: V)

    fun emitIntent(): Observable<I>

    fun emitIntentWithoutAction(): Observable<I> = Observable.never<I>()
}