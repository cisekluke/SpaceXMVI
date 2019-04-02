package co.untitledkingdom.spacexmvi.main

import co.untitledkingdom.spacexmvi.base.MvvmiViewState
import co.untitledkingdom.spacexmvi.models.Rocket
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainViewState(
    val progress: Boolean = false,
    val error: Boolean = false,
    val rocketList: List<Rocket> = listOf(),
    val displayFragment: Boolean = false
) : MvvmiViewState