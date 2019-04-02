package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.MvvmiView
import co.untitledkingdom.spacexmvi.base.MvvmiViewState
import co.untitledkingdom.spacexmvi.base.MvvmiModel
import junit.framework.Assert

abstract class BaseMviViewRobot<V : MvvmiView<*, *>, out M : MvvmiModel<*, V, *>, S : MvvmiViewState>(
    private val viewModel: M
) {

    protected abstract val renderedStates: ArrayList<in S>
    protected abstract val view: V

    internal fun assertViewStates(vararg expectedStates: S) {
        Assert.assertEquals(expectedStates.toList(), renderedStates)
    }

    internal fun startView() {
        viewModel.attachView(view)
        viewModel.bind()
    }

    internal fun stopView() {
        viewModel.unbind()
    }
}