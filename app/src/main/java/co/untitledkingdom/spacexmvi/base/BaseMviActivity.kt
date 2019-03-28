package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<VS : BaseMviViewState, V : BaseMviView<VS, *>, M : BaseViewModel<VS, V, *>> :
    AppCompatActivity() {

    private lateinit var viewModel: M
    private val bundleKey = "ACTIVITY_BUNDLE"

    protected abstract fun getView(): V
    protected abstract fun getViewModel(): M

    override fun onCreate(savedInstanceState: Bundle?) {
        injection()
        super.onCreate(savedInstanceState)

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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(bundleKey, viewModel.getViewState())
        super.onSaveInstanceState(outState)
    }

    protected open fun injection() {}

    private fun restoreStateIfExists(savedInstanceState: Bundle?) {
        if (!viewModel.isAlreadyInitialized() && savedInstanceState != null) {
            viewModel.setInitialViewState(
                savedInstanceState.getParcelable(bundleKey)
            )
        }
    }

    private fun initialize() {
        viewModel.attachView(getView())
    }
}