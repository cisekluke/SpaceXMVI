package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseMviActivity<VS : BaseMviViewState, V : BaseMviView<VS>, P : BaseMviPresenter<VS, V, *>> :
    AppCompatActivity() {

    private lateinit var presenter: P
    private val retainedTag = "HOLDER"
    private val key = "ACTIVITY_BUNDLE"

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

    abstract fun getView(): V

    abstract fun getPresenter(): P

    private fun initialize() {
        presenter.attachView(getView())
    }

    // TODO do something with else
    @Suppress("UNCHECKED_CAST")
    private fun attachPresenter() {
        var retainedFragment = supportFragmentManager.findFragmentByTag(retainedTag)

        if (retainedFragment == null) {
            retainedFragment = BaseRetainedFragment<VS, P>()
            supportFragmentManager.beginTransaction()
                .add(retainedFragment, retainedTag).commit()

            presenter = getPresenter()
            retainedFragment.setPresenter(presenter, key)
        } else {
            presenter =
                (retainedFragment as BaseRetainedFragment<VS, P>).getPresenter() ?: getPresenter()
            retainedFragment.setPresenter(presenter, key)
            retainedFragment.getInfoFromBundle()
        }
    }
}