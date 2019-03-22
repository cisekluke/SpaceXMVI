package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<V : BaseMviView<*>, P : BaseMviPresenter<*, V, *>> :
    AppCompatActivity() {

    private lateinit var presenter: P
    private val retainedTag = "HOLDER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachPresenter()
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

    override fun onRetainCustomNonConfigurationInstance(): P {
        return presenter
    }

    abstract fun getView(): V

    abstract fun getPresenter(): P

    private fun initialize() {
        presenter.attachView(getView())
    }

    @Suppress("UNCHECKED_CAST")
    private fun attachPresenter() {
        var retainedFragment = supportFragmentManager.findFragmentByTag(retainedTag)

        if (retainedFragment == null) {
            retainedFragment = BaseRetainedFragment<P>()
            supportFragmentManager.beginTransaction()
                .add(retainedFragment, retainedTag).commit()

            presenter = getPresenter()
            retainedFragment.setPresenter(presenter)
        } else {
            presenter = (retainedFragment as BaseRetainedFragment<P>).getPresenter()
        }
    }
}