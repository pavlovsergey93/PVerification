package com.gmail.pavlovsv93.verification.di

import androidx.lifecycle.MutableLiveData
import com.gmail.pavlovsv93.verification.data.remote.RemoteDataSource
import com.gmail.pavlovsv93.verification.data.remote.RemoteRepository
import com.gmail.pavlovsv93.verification.data.remote.RemoteRepositoryInterface
import com.gmail.pavlovsv93.verification.domain.datasource.RemoteInterface
import com.gmail.pavlovsv93.verification.ui.remove.RemoveInVerificationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val verificationModule = module {
    single<RemoteRepositoryInterface> { RemoteRepository(db = get<FirebaseFirestore>()) }
    single<RemoteInterface> { RemoteDataSource(repository = get<RemoteRepositoryInterface>())}
    viewModel<RemoveInVerificationViewModel>(named("RemoveInVerificationViewModel")) {
        RemoveInVerificationViewModel(
            liveData = MutableLiveData(),
            dataSource = get<RemoteInterface>(),
        )
    }
}