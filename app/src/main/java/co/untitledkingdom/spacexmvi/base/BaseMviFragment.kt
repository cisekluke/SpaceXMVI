package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<VS : BaseMviViewState, V : BaseMviView<VS>, P : BaseMviPresenter<VS, V, *>> :
    Fragment() {

    private lateinit var presenter: P
    private val retainedTag = "FRAGMENT_HOLDER"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        injection()
        super.onActivityCreated(savedInstanceState)

        attachPresenter()
        initialize()
    }

    protected open fun injection() {}

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

    abstract fun view(): V

    abstract fun getPresenter(): P

    private fun initialize() {
        presenter.attachView(view())
    }

    private fun attachPresenter() {
        if (!retainedFragmentHasInstance()) {
            createRetainedFragment()
        } else {
            getPresenterFromRetainedFragment()
        }
    }

    private fun retainedFragmentHasInstance() =
        (findRetainedFragment() != null)

    private fun createRetainedFragment() {
        val retainedFragment = BaseRetainedFragment<VS, P>()
        childFragmentManager.beginTransaction()
            .add(retainedFragment, retainedTag).commit()

        presenter = getPresenter()
        setPresenterInstance(retainedFragment)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPresenterFromRetainedFragment() {
        val retainedFragment = findRetainedFragment()

        presenter =
            (retainedFragment as BaseRetainedFragment<VS, P>).getPresenter() ?: getPresenter()
        setPresenterInstance(retainedFragment)

        retainedFragment.getInfoFromBundle()
    }

    private fun setPresenterInstance(retainedFragment: BaseRetainedFragment<VS, P>) {
        retainedFragment.setPresenter(presenter)
    }

    private fun findRetainedFragment() = childFragmentManager.findFragmentByTag(retainedTag)
}