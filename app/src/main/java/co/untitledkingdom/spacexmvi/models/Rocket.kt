package co.untitledkingdom.spacexmvi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rocket(val name: String = "", val photoUrl: String = "") : Parcelable