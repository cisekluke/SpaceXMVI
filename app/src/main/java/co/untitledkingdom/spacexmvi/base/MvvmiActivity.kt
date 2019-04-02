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
abstract class MvvmiActivity<VS : MvvmiViewState, V : MvvmiView<VS, *>, M : MvvmiModel<VS, V, *>> :
    AppCompatActivity() {

    private lateinit var viewModel: M
    private val bundleKey = "ACTIVITY_BUNDLE"

    /**
     * Following official Dagger&Android documentation we need to perform injection before super.onCreate call.
     * Read the <a href="https://google.github.io/dagger/android#when-to-inject">Google documentation</a>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        injection()
        super.onCreate(savedInstanceState)

        viewModel = setViewModel()

        restoreStateIfExists(savedInstanceState)
        initialize()
    }

    /**
     * Not every project has Dagger dependencies and also, it's API changes so it's open to fill by custom
     * implementation.
     */
    protected open fun injection() {}

    /**
     * Set ViewModel by calling this method inside feature Activity.
     *
     * @return MvvmiModel
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

    /**
     * This method is being used to load state from the Bundle only in case that we can't restore it from
     * the ViewModel.
     *
     * @return if true that means there's a new instance of ViewModel
     */
    private fun newViewModelHasBeenCreated() = !viewModel.isAlreadyInitialized()

    /**
     * Sending View to the ViewModel.
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

    /**
     * Calls ViewModel method that will start subscription.
     */
    override fun onStart() {
        super.onStart()
        viewModel.bind()
    }

    /**
     * Calls ViewModel method that will clear it's Disposables. This needs to be called in that lifecycle method
     * to avoid memory leaks.
     */
    override fun onStop() {
        viewModel.unbind()
        super.onStop()
    }

    /**
     * Calls ViewModel method that will change flag inside it so it will know that View has been destroyed and
     * it should subscribe to intent emitter again.
     */
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