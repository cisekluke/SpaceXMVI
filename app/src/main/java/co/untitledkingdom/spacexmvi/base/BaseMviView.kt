package co.untitledkingdom.spacexmvi.base

interface BaseMviView<in V : BaseMviViewState> {

    fun render(viewState: V)
}