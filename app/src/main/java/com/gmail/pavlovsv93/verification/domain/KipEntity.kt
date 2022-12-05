package com.gmail.pavlovsv93.verification.domain

import com.google.firebase.Timestamp

data class KipEntity(
	val idKip: String,
	val number: String,
	var date: Timestamp,
	val model: String,
	val parameter: String,
	var keyStation: Long,
	var station: String,
	var position: String,
	var description: String = "No description",
	var nextDate: Timestamp,
	var status: String,
	var verification: Long = 1L,
	var info: String = "No info"
)
