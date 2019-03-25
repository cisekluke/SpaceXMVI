package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

class BaseRetainedFragment<VS : BaseMviViewState, P : BaseMviPresenter<VS, *, *>> : Fragment() {

    private var presenter: P? = null
    private val key = "FRAGMENT_BUNDLE"
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        bundle = savedInstanceState
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter!!.saveLastViewState(key, outState)
        super.onSaveInstanceState(outState)
    }

    fun getInfoFromBundle() {
        bundle?.let { savedInstance ->
            Log.d("xDD", "presenter after: $presenter")
            presenter!!.initState(savedInstance.getParcelable<VS>(key))
        }
    }

    fun setPresenter(presenter: P) {
        this.presenter = presenter
    }

    fun getPresenter() = presenter
}