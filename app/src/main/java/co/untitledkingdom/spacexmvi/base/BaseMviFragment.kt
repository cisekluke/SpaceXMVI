package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<VS : BaseMviViewState, A : BaseMviActivity<*, *, *>, V : BaseMviView<VS, *>, in M : BaseViewModel<VS, V, *>>(
    private val modelClass: Class<M>
) : Fragment() {

    private lateinit var viewModel: M
    private val key = "FRAGMENT_BUNDLE"

    protected abstract fun view(): V

    protected open fun viewModelInitialize(fragmentActivity: A? = null) {
        viewModel =
            if (fragmentActivity != null) ViewModelProviders.of(fragmentActivity).get(modelClass)
            else ViewModelProviders.of(this).get(modelClass)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelInitialize()

        if (!viewModel.isInitialized && savedInstanceState != null) viewModel.setInitialViewState(
            savedInstanceState.getParcelable(key)
        )

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

    private fun initialize() {
        viewModel.attachView(view())
    }
}