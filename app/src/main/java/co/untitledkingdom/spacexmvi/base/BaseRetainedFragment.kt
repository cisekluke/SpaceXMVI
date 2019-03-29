package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

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

    // TODO think if should be any calls to presenter from that class
    override fun onSaveInstanceState(outState: Bundle) {
        presenter?.saveLastViewState(key, outState)
        super.onSaveInstanceState(outState)
    }

    fun getInfoFromBundle() = previousInstance?.getParcelable<VS>(key)

    fun setPresenter(presenter: P) {
        this.presenter = presenter
    }

    fun getPresenter() = presenter
}