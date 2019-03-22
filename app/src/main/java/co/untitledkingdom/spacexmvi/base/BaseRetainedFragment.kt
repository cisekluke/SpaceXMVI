package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

class BaseRetainedFragment<P : BaseMviPresenter<*, *, *>> : Fragment() {

    private lateinit var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun setPresenter(presenter: P) {
        this.presenter = presenter
    }

    fun getPresenter() = presenter
}