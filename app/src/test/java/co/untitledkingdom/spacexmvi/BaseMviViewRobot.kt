package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseMviView
import co.untitledkingdom.spacexmvi.base.BaseMviViewState
import co.untitledkingdom.spacexmvi.base.BaseViewModel
import junit.framework.Assert

abstract class BaseMviViewRobot<V : BaseMviView<*>, out M : BaseViewModel<*, V, *>, S : BaseMviViewState>(private val viewModel: M) {

    protected abstract val renderedStates: ArrayList<in S>
    protected abstract val mainView: V

    /** compares expected states with rendered */
    internal fun assertViewStates(vararg expectedStates: S) {
        Assert.assertEquals(expectedStates.toList(), renderedStates)
    }

    /** simulates onStart */
    internal fun startView() {
        viewModel.attachView(mainView)
        viewModel.bind()
    }

    /** simulates onStop */
    internal fun stopView() {
        viewModel.unbind()
    }
}