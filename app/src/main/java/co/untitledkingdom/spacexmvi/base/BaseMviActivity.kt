package co.untitledkingdom.spacexmvi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Base class to implement for activities to follow our MVVMI architecture approach.
 *
 * @param VS declares view state class
 * @param V stands for View interface
 * @param M presentation layer which is lifecycle awareness
 */
abstract class BaseMviActivity<VS : BaseMviViewState, V : BaseMviView<VS, *>, M : BaseViewModel<VS, V, *>> :
    AppCompatActivity() {

    private lateinit var viewModel: M
    private val bundleKey = "ACTIVITY_BUNDLE"

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Following official Dagger&Android documentation we need to perform injection before super.onCreate call.
         * Read the <a href="https://google.github.io/dagger/android#when-to-inject">Google documentation</a>
         */
        injection()
        super.onCreate(savedInstanceState)

        viewModel = setViewModel()

        restoreStateIfExists(savedInstanceState)
        initialize()
    }

    /**
     * Not every project has Dagger and also, it's API changes so it's open to fill by custom implementation.
     */
    protected open fun injection() {}

    /**
     * Set ViewModel by calling this method inside feature Activity.
     *
     * @return BaseViewModel
     */
    protected abstract fun setViewModel(): M

    /**
     * In case that ViewModel has been destroyed but we still want to restore previous state, for example,
     * when process has been killed by Android when the App was in the background. State is being restored
     * from the Bundle and passed to the freshly initialized ViewModel.
     *
     * @param savedInstanceState comes from #onCreate method.
     */
    private fun restoreStateIfExists(savedInstanceState: Bundle?) {
        if (newViewModelHasBeenCreated() && savedInstanceState != null) {
            viewModel.setInitialViewState(
                savedInstanceState.getParcelable(bundleKey)
            )
        }
    }

    private fun newViewModelHasBeenCreated() = !viewModel.isAlreadyInitialized()

    /**
     * Attaching View to the Presentation layer.
     */
    private fun initialize() {
        viewModel.attachView(setView())
    }

    /**
     * Override inside feature Activity by returning itself.
     *
     * @return View interface
     */
    protected abstract fun setView(): V

    override fun onStart() {
        super.onStart()
        viewModel.bind()
    }

    override fun onStop() {
        viewModel.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.unsubscribe()
        super.onDestroy()
    }

    /**
     * Save state to bundle in cases that ViewModel will be destroyed @see restoreStateIfExists(Bundle?)
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(bundleKey, viewModel.getViewState())
        super.onSaveInstanceState(outState)
    }
}