package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<VS : BaseMviViewState, V : BaseMviView<VS>, M : BaseViewModel<VS, V, *>>
    : AppCompatActivity() {

    private lateinit var viewModel: M
    private val bundleKey = "ACTIVITY_BUNDLE"

    override fun onCreate(savedInstanceState: Bundle?) {
        injection()
        super.onCreate(savedInstanceState)

        viewModel = setViewModel()

        restoreStateIfExists(savedInstanceState)
        initialize()
    }

    protected open fun injection() {}

    abstract fun setViewModel(): M

    private fun restoreStateIfExists(savedInstanceState: Bundle?) {
        if (newViewModelHasBeenCreated() && savedInstanceState != null) {
            viewModel.setInitialViewState(
                savedInstanceState.getParcelable(bundleKey)
            )
        }
    }

    private fun newViewModelHasBeenCreated() = !viewModel.isAlreadyInitialized()

    private fun initialize() {
        viewModel.attachView(setView())
    }

    abstract fun setView(): V

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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(bundleKey, viewModel.getViewState())
        super.onSaveInstanceState(outState)
    }
}