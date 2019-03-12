package co.untitledkingdom.spacexmvi.base

interface BaseMviPartialState<V : BaseMviViewState> {

    fun reduce(previousState: V): V
}