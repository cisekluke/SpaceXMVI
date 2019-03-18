package co.untitledkingdom.spacexmvi.base


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<A : BaseMviActivity<*, *>, V : BaseMviView<*, *>, in M : BaseViewModel<*, V, *>>(
    private val modelClass: Class<M>
) : Fragment() {

    private lateinit var viewModel: M

    private lateinit var context: A

    protected abstract fun view(): V

    protected abstract fun setActivity(): A

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(setActivity()).get(modelClass)
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