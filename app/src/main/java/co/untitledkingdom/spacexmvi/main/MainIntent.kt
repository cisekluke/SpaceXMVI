package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.MvvmiIntent

sealed class MainIntent : MvvmiIntent {
    object FetchRocketsState : MainIntent()
    object ClearState : MainIntent()
    data class DisplayFragmentState(private val showFragment: Boolean = false) : MainIntent()
}