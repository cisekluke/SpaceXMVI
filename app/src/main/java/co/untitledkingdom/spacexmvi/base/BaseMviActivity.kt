package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<V : BaseMviView<*>, P : BaseMviPresenter<*, V, *>>(

) : AppCompatActivity() {

    private lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = getPresenter()
        initialize()
    }

    override fun onStart() {
        super.onStart()
        presenter.bind()
    }

    override fun onStop() {
        presenter.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        presenter.deinitialize()
        super.onDestroy()
    }

    abstract fun getView(): V

    abstract fun getPresenter(): P

    private fun initialize() {
        presenter.attachView(getView())
    }
}