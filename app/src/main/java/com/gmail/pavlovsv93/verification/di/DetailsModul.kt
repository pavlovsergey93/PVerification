package com.gmail.pavlovsv93.verification.di

import androidx.lifecycle.MutableLiveData
import com.gmail.pavlovsv93.verification.data.details.DetailsDataSource
import com.gmail.pavlovsv93.verification.data.details.DetailsRepository
import com.gmail.pavlovsv93.verification.data.details.DetailsRepositoryInterface
import com.gmail.pavlovsv93.verification.domain.datasource.DetailsInterface
import com.gmail.pavlovsv93.verification.ui.detailsdevice.DetailsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val detailsModule = module {
    single<DetailsRepositoryInterface> { DetailsRepository(db = get<FirebaseFirestore>()) }
    single<DetailsInterface> { DetailsDataSource(repository = get<DetailsRepositoryInterface>()) }
    viewModel<DetailsViewModel>(named("DetailsViewModel")) {
        DetailsViewModel(
            liveData = MutableLiveData(),
            dataSource = get<DetailsInterface>()
        )
    }
}