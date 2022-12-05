package com.gmail.pavlovsv93.verification.data.realtime.entity

data class Entity constructor(
	val uid: String = "",
	val model: String = "",
	val number: String = "",
	val parameter: String = "",
	val date: String = "",
	val location: String = "",
	var status: String = "",
	val verification: Int = 1
)
