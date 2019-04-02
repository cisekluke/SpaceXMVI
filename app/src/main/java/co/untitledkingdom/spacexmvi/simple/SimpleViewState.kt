package co.untitledkingdom.spacexmvi.simple

import co.untitledkingdom.spacexmvi.base.MvvmiViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SimpleViewState(val output: String = "0") : MvvmiViewState