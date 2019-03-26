package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

class BaseRetainedFragment<VS : BaseMviViewState, P : BaseMviPresenter<VS, *, *>> : Fragment() {

    private var presenter: P? = null
    // TODO think about those keys if one is enough or it should be provided by child
    private var key = "FRAGMENT_BUNDLE"
    private var previousInstance: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        previousInstance = savedInstanceState
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter?.saveLastViewState(key, outState)
        super.onSaveInstanceState(outState)
    }

    fun getInfoFromBundle() {
        previousInstance?.let { savedInstance ->
            Log.d("xDDD", "${savedInstance.getParcelable<VS>(key)}")
            presenter?.initState(savedInstance.getParcelable(key))
        }
    }

    fun setPresenter(presenter: P, key :String) {
        this.presenter = presenter
        this.key = key
    }

    fun getPresenter() = presenter
}