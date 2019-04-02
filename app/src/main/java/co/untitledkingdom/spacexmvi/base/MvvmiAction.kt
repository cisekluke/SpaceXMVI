package co.untitledkingdom.spacexmvi.base

interface MvvmiAction<V : MvvmiViewState> {

    fun reduce(previousState: V): V
}