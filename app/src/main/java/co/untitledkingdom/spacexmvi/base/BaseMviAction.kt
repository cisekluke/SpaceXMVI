package co.untitledkingdom.spacexmvi.base

interface BaseMviAction<V : BaseMviViewState> {

    fun reduce(previousState: V): V
}