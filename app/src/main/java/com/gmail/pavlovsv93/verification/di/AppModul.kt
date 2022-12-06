package com.gmail.pavlovsv93.verification.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val appModule = module {
	single<FirebaseFirestore> { FirebaseFirestore.getInstance()	}
}