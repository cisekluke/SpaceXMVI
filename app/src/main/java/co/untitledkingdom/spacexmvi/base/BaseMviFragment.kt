package co.untitledkingdom.spacexmvi.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<VS : BaseMviViewState, A : BaseMviActivity<*, *, *>, V : BaseMviView<VS, *>, M : BaseViewModel<VS, V, *>> :
    Fragment() {

    private lateinit var viewModel: M
    private val bundleKey = "FRAGMENT_BUNDLE"

    protected abstract fun view(): V

    override fun onAttach(context: Context?) {
        injection()
        super.onAttach(context)
    }

    protected open fun injection() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = getViewModel()

        restoreStateIfExists(savedInstanceState)
        initialize()
    }

    protected abstract fun getViewModel(): M

    private fun restoreStateIfExists(savedInstanceState: Bundle?) {
        if (newViewModelHasBeenCreated() && savedInstanceState != null) {
            viewModel.setInitialViewState(
                savedInstanceState.getParcelable(bundleKey)
            )
        }
    }

    private fun newViewModelHasBeenCreated() = !viewModel.isAlreadyInitialized()

    private fun initialize() {
        viewModel.attachView(view())
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
        outState.putParcelable(bundleKey, viewModel.getViewState())
        super.onSaveInstanceState(outState)
    }
}