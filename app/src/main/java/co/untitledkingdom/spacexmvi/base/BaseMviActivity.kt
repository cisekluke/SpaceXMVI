package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

// TODO think about adding base method to do injection
abstract class BaseMviActivity<VS : BaseMviViewState, V : BaseMviView<VS>, M : BaseViewModel<VS, V, *>>
    : AppCompatActivity() {

    private lateinit var viewModel: M
    private val key = "ACTIVITY_BUNDLE"

    abstract fun getView(): V

    abstract fun getViewModel(): M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel()

        if (!viewModel.isAlreadyInitialized() && savedInstanceState != null) {
            viewModel.setInitialViewState(
                savedInstanceState.getParcelable(key)
            )
        }

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
        outState?.putParcelable(key, viewModel.getViewState())
        super.onSaveInstanceState(outState)
    }

    private fun initialize() {
        viewModel.attachView(getView())
    }
}