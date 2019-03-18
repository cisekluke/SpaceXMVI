package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseMviIntent

sealed class MainIntent : BaseMviIntent {
    object FetchRocketsState : MainIntent()
    object ClearState : MainIntent()
    data class DisplayFragmentState(private val showFragment: Boolean = false) : MainIntent()
}