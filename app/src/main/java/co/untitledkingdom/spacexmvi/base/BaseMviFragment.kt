package co.untitledkingdom.spacexmvi.base

import android.app.Fragment
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle

abstract class BaseMviFragment<A : BaseMviActivity<*, *>, V : BaseMviView<*, *>, in M : BaseViewModel<*, V, *>>(
    private val modelClass: Class<M>,
    private val activity: A
) : Fragment() {

    private lateinit var viewModel: M

    protected abstract fun view(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity).get(modelClass)
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