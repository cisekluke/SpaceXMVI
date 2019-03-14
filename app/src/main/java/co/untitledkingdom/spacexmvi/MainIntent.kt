package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.base.BaseMviIntent

sealed class MainIntent : BaseMviIntent {
    object FetchRocketsState : MainIntent()
    object ClearState : MainIntent()
}