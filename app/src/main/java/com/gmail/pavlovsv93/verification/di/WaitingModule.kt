package com.gmail.pavlovsv93.verification.di

import androidx.lifecycle.MutableLiveData
import com.gmail.pavlovsv93.verification.data.waiting.WaitingDataSource
import com.gmail.pavlovsv93.verification.data.waiting.WaitingRepository
import com.gmail.pavlovsv93.verification.data.waiting.WaitingRepositoryInterface
import com.gmail.pavlovsv93.verification.domain.datasource.WaitingInterface
import com.gmail.pavlovsv93.verification.ui.waitreturn.WaitingForReturnViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val waitingModule = module {

    single<WaitingRepositoryInterface> { WaitingRepository(db = get<FirebaseFirestore>()) }
    single<WaitingInterface> { WaitingDataSource(repository = get<WaitingRepositoryInterface>()) }
    viewModel<WaitingForReturnViewModel>(named("WaitingForReturnViewModel")) {
        WaitingForReturnViewModel(
            liveData = MutableLiveData(),
            dataSource = get<WaitingInterface>()
        )
    }
}