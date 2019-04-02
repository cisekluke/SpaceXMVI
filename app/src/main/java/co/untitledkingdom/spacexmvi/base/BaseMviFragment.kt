package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMviFragment<VS : BaseMviViewState, V : BaseMviView<VS>, P : BaseMviPresenter<VS, V, *>> :
    Fragment() {

    private lateinit var featurePresenter: P
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
        featurePresenter.bind()
    }

    override fun onStop() {
        featurePresenter.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        featurePresenter.deinitialize()
        super.onDestroy()
    }

    abstract fun view(): V

    abstract fun getPresenter(): P

    private fun initialize() {
        featurePresenter.attachView(view())
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

        featurePresenter = getPresenter()
        setPresenterInstance(retainedFragment)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPresenterFromRetainedFragment() {
        val retainedFragment = findRetainedFragment()

        featurePresenter =
            (retainedFragment as BaseRetainedFragment<VS, P>).getPresenter() ?: getPresenter()
        setPresenterInstance(retainedFragment)

        retainedFragment.getInfoFromBundle()?.let { featurePresenter.initState(it) }
    }

    private fun setPresenterInstance(retainedFragment: BaseRetainedFragment<VS, P>) {
        retainedFragment.setPresenter(featurePresenter)
    }

    private fun findRetainedFragment() = childFragmentManager.findFragmentByTag(retainedTag)
}