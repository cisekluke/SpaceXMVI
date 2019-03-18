package co.untitledkingdom.spacexmvi.base


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<A : BaseMviActivity<*, *>, V : BaseMviView<*, *>, in M : BaseViewModel<*, V, *>>(
    private val modelClass: Class<M>,
    private val activity: A
) : Fragment() {

    private lateinit var viewModel: M

     open lateinit var parent: A

    protected abstract fun view(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(parent).get(modelClass)
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