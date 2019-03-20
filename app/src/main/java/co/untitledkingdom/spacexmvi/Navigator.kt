package co.untitledkingdom.spacexmvi

import android.content.Intent
import android.view.View
import co.untitledkingdom.spacexmvi.base.BaseMviActivity
import co.untitledkingdom.spacexmvi.main.MainActivity
import co.untitledkingdom.spacexmvi.main.MainView
import co.untitledkingdom.spacexmvi.main.MainViewModel
import co.untitledkingdom.spacexmvi.simple.SimpleFragment
import kotlinx.android.synthetic.main.activity_main.fragmentContainer

class Navigator(private val activity: BaseMviActivity<MainView, MainViewModel>) {

    private val defaultTag = "TAG"

    fun displayFragment(showFragment: Boolean) {
        if (showFragment) {
            removeFragment()
            activity.fragmentContainer.visibility = View.VISIBLE
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, SimpleFragment(), defaultTag)
                .commit()
        }
    }

    private fun removeFragment() {
        activity.supportFragmentManager.findFragmentByTag(defaultTag)?.let {
            activity.fragmentContainer.visibility = View.GONE
            activity.supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }

    fun startActivity() {
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }
}