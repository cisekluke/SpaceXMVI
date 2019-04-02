package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.MvvmiIntent

sealed class SimpleIntent : MvvmiIntent {
    data class ShowOutputStage(val input: String = "") : SimpleIntent()
}