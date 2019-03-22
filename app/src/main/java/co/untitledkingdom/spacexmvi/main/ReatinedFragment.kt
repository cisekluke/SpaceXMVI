package co.untitledkingdom.spacexmvi.main

import android.os.Bundle
import android.support.v4.app.Fragment

class ReatinedFragment : Fragment() {

     lateinit var presenter: MainMviPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun setPres(presenter: MainMviPresenter) {
        this.presenter = presenter
    }

    fun getPres() = presenter
}