package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.BaseMviViewState
import co.untitledkingdom.spacexmvi.models.Rocket

data class MainViewState(
    val progress: Boolean = false,
    val error: Boolean = false,
    val rocketList: List<Rocket> = listOf(),
    val displayFragment: Boolean = false
) : BaseMviViewState