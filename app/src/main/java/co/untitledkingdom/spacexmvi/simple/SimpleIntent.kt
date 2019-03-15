package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.BaseMviIntent

sealed class SimpleIntent : BaseMviIntent {
    data class ShowOutputStage(val input: String = "") : SimpleIntent()
}