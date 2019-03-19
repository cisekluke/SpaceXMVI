package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseMviView
import co.untitledkingdom.spacexmvi.base.BaseMviViewState
import co.untitledkingdom.spacexmvi.base.BaseMviPresenter
import junit.framework.Assert

abstract class BaseMviViewRobot<V : BaseMviView<*>, out M : BaseMviPresenter<*, V, *>, S : BaseMviViewState>(
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