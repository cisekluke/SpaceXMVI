package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<V : BaseMviView<*>, in M : BaseViewModel<*, V, *>>(
    private val modelClass: Class<M>
) : AppCompatActivity() {

    private lateinit var viewModel: M

    abstract fun getView(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(modelClass)
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

    private fun initialize() {
        viewModel.attachView(getView())
    }
}