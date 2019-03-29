package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<VS : BaseMviViewState, V : BaseMviView<VS>, P : BaseMviPresenter<VS, V, *>> :
    Fragment() {

    private lateinit var presenter: P
    private val retainedTag = "FRAGMENT_HOLDER"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        (activity?.supportFragmentManager?.findFragmentByTag(retainedTag) != null)

    private fun createRetainedFragment() {
        val retainedFragment = BaseRetainedFragment<VS, P>()
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(retainedFragment, retainedTag)?.commit()

        presenter = getPresenter()
        setPresenterInstance(retainedFragment)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPresenterFromRetainedFragment() {
        val retainedFragment = activity?.supportFragmentManager?.findFragmentByTag(retainedTag)

        presenter =
            (retainedFragment as BaseRetainedFragment<VS, P>).getPresenter() ?: getPresenter()
        setPresenterInstance(retainedFragment)

        retainedFragment.getInfoFromBundle()
    }

    private fun setPresenterInstance(retainedFragment: BaseRetainedFragment<VS, P>) {
        retainedFragment.setPresenter(presenter)
    }
}