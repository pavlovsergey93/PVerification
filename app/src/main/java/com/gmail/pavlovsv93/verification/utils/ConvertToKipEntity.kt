package com.gmail.pavlovsv93.verification.utils

import com.gmail.pavlovsv93.verification.*
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

fun convertToKipEntity(item: DocumentSnapshot ): KipEntity{
    val idKip = item.id
    val date = item.data?.get(KEY_DATE)
    val number = item.data?.get(KEY_NUMBER)
    val parameter = item.data?.get(KEY_PARAMETER)
    val description = item.data?.get(KEY_DESCRIPTION)
    val model = item.data?.get(KEY_MODEL)
    val keyStation = item.data?.get(KEY_STATION_KEY)
    val station = item.data?.get(KEY_STATION)
    val position = item.data?.get(KEY_POSITION)
    val nextDate = item.data?.get(KEY_NEXT_DATE)
    val verification = item.data?.get(KEY_VERIFICATION)
    val status = item.data?.get(KEY_STATUS)
    val info = item.data?.get(KEY_INFO)
    return KipEntity(
        idKip = idKip,
        number = number as String,
        date = date as Timestamp,
        model = model as String,
        parameter = parameter as String,
        keyStation = keyStation as Long,
        station = station as String,
        position = position as String,
        description = description as String,
        nextDate = nextDate as Timestamp,
        status = status as String,
        verification = verification as Long,
        info = info as String
    )
}