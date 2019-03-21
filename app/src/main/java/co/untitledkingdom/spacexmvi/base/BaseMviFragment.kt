package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<V : BaseMviView<*>, P : BaseMviPresenter<*, V, *>> :
    Fragment() {

    private lateinit var presenter: P

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
//
//    override fun onRetainCustomNonConfigurationInstance(): P {
//        return presenter
//    }

    abstract fun view(): V

    abstract fun getPresenter(): P

    private fun initialize() {
        presenter.attachView(view())
    }

//    private fun attachPresenter() {
//        presenter =
//            if (lastCustomNonConfigurationInstance != null) lastCustomNonConfigurationInstance as P
//            else getPresenter()
//    }
}