package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseMviView
import co.untitledkingdom.spacexmvi.base.BaseMviViewState
import co.untitledkingdom.spacexmvi.base.BaseViewModel
import junit.framework.Assert

abstract class BaseMviViewRobot<V : BaseMviView<*>, S : BaseMviViewState, M : BaseViewModel<*, V, *>> {

    abstract val renderedStates: ArrayList<S>
    abstract val viewModel: M
    abstract val mainView: V

    internal fun assertViewStates(vararg expectedStates: S) {
        Assert.assertEquals(expectedStates.toList(), renderedStates)
    }

    fun startView() {
        viewModel.attachView(mainView)
        viewModel.bind()
    }

    fun stopView() {
        viewModel.unbind()
    }
}