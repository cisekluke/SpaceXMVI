package co.untitledkingdom.spacexmvi.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<V : BaseMviView<*>, in M : BaseViewModel<*, V, *>>(
        private val modelClass: Class<M>
) : AppCompatActivity() {

    private lateinit var viewModel: M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(modelClass)
    }

    override fun onStart() {
        super.onStart()
        viewModel.bind()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unbind()
    }

    protected abstract fun initializeViewModel()

    protected fun initialize(view: V) {
        viewModel.attachView(view)
    }
}