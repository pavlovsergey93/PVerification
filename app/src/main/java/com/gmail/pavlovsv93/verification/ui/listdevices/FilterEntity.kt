package com.gmail.pavlovsv93.verification.ui.listdevices

import android.os.Parcel
import android.os.Parcelable

data class FilterEntity(
	var isCheckedData: Boolean? = null,
	var model:String? = null,
	var station:String? = null,
	var parameter:String? = null,
	var status:String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(isCheckedData)
		parcel.writeString(model)
		parcel.writeString(station)
		parcel.writeString(parameter)
		parcel.writeString(status)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<FilterEntity> {
		override fun createFromParcel(parcel: Parcel): FilterEntity {
			return FilterEntity(parcel)
		}

		override fun newArray(size: Int): Array<FilterEntity?> {
			return arrayOfNulls(size)
		}
	}
}

