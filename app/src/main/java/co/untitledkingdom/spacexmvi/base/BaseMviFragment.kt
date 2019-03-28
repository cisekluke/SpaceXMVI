package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<VS : BaseMviViewState, A : BaseMviActivity<*, *, *>, V : BaseMviView<VS, *>, M : BaseViewModel<VS, V, *>> :
    Fragment() {

    private lateinit var viewModel: M
    private val key = "FRAGMENT_BUNDLE"

    protected abstract fun view(): V

    protected abstract fun getViewModel(): M

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        injection()
        super.onActivityCreated(savedInstanceState)

        viewModel = getViewModel()

        restoreStateIfExists(savedInstanceState)
        initialize()
    }

    override fun onStart() {
        super.onStart()
        viewModel.bind()
    }

    override fun onStop() {
        viewModel.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.unsubscribe()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(key, viewModel.getViewState())
        super.onSaveInstanceState(outState)
    }

    protected open fun injection() {}

    private fun restoreStateIfExists(savedInstanceState: Bundle?) {
        if (!viewModel.isAlreadyInitialized() && savedInstanceState != null) {
            viewModel.setInitialViewState(
                savedInstanceState.getParcelable(key)
            )
        }
    }

    private fun initialize() {
        viewModel.attachView(view())
    }
}