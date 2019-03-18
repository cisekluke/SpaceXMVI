package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<A : BaseMviActivity<*, *>, V : BaseMviView<*, *>, in M : BaseViewModel<*, V, *>>(
    private val modelClass: Class<M>
) : Fragment() {

    private lateinit var viewModel: M

    protected abstract fun view(): V

    protected open fun viewModelInitialize(activity: A? = null) {
        viewModel = if (activity != null) ViewModelProviders.of(activity).get(modelClass)
        else ViewModelProviders.of(this).get(modelClass)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelInitialize()
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

    private fun initialize() {
        viewModel.attachView(view())
    }
}