package co.untitledkingdom.spacexmvi.base

interface BaseMviView<V : BaseMviViewState> {

    fun render(viewState: V)
}