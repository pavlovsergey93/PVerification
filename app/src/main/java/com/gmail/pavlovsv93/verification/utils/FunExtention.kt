package com.gmail.pavlovsv93.verification.utils

import android.os.Build
import com.gmail.pavlovsv93.verification.*
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun dataFormat(date: Timestamp?): String{
	val format = SimpleDateFormat("dd.MM.yyyy")
	var dateFormat = "00.00.0000"
	date?.let {
		dateFormat = format.format(date.toDate())
	}
	return dateFormat
}
fun dateParsed(dateStr: String): Timestamp{
	val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		DateTimeFormatter.ofPattern("dd.MM.yyyy")
	} else {
		TODO("VERSION.SDK_INT < O")
	}
	val date = LocalDate.parse(dateStr, format)
	return Timestamp(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
}

fun setBackgroundStatus(status: String): Int {
	return when (status) {
		STATUS_INSTALLED -> {
			R.color.installed
		}
		STATUS_REMOVED->{
			R.color.removed
		}
		STATUS_VERIFICATION -> {
			R.color.verification
		}
		STATUS_REJECTED -> {
			R.color.rejected
		}
		STATUS_FIX -> {
			R.color.fix
		}
		STATUS_REMOTE -> {
			R.color.remote
		}
		STATUS_TRUSTED -> {
			R.color.trusted
		}
		STATUS_SAVE -> {
			android.R.color.holo_orange_light
		}
		STATUS_SAVED -> {
			android.R.color.holo_orange_light
		}
		else -> {
			R.color.no_status
		}
	}
}

fun setImageDevice(model: String): Int{
	return when (model) {
		MODEL_SSS_07_08 -> {
			R.drawable.sss903_0708
		}
		MODEL_SSS_08_14 -> {
			R.drawable.sss903
		}
		MODEL_SSS_M -> {
			R.drawable.sss903mtme
		}
		MODEL_SSS_MT -> {
			R.drawable.sss903mtme
		}
		MODEL_SSS_ST -> {
			R.drawable.sss903st
		}
		MODEL_SSS_Mst -> {
			R.drawable.sss903st
		}
		MODEL_GANK -> {
			R.drawable.gank_4c
		}
		MODEL_DAK -> {
			R.drawable.dah
		}
		MODEL_DAH_M -> {
			R.drawable.dah_m
		}
		MODEL_TESTO_6621 -> {
			R.drawable.testo_6621
		}
		MODEL_TESTO_6651 -> {
			R.drawable.testo_6651
		}
		MODEL_TSPU -> {
			R.drawable.tspu_205
		}
		MODEL_PVT -> {
			R.drawable.gdn_100
		}
		MODEL_IPTV -> {
			R.drawable.bgnd_206
		}
		MODEL_DGS -> {
			R.drawable.dgs_125
		}
		MODEL_TESTO_VELOSITY -> {
			R.drawable.ic_baseline_image_24
		}
		MODEL_SRPS -> {
			R.drawable.srps_05d
		}
		MODEL_BDKG -> {
			R.drawable.bdkg204
		}
		MODEL_AEROKON -> {
			R.drawable.aerokon
		}
		//todo Дописать остальные модели приборов
		else -> {
			R.drawable.ic_baseline_image_24
		}
	}
}