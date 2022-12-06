package com.gmail.pavlovsv93.verification.di

import androidx.lifecycle.MutableLiveData
import com.gmail.pavlovsv93.verification.data.devices.DevicesDataSource
import com.gmail.pavlovsv93.verification.data.devices.DevicesRepository
import com.gmail.pavlovsv93.verification.data.devices.DevicesRepositoryInterface
import com.gmail.pavlovsv93.verification.domain.datasource.DevicesInterface
import com.gmail.pavlovsv93.verification.ui.listdevices.ListDeviceViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val devicesModule = module {
    single<DevicesRepositoryInterface> { DevicesRepository(db = get<FirebaseFirestore>()) }
    single<DevicesInterface> { DevicesDataSource(repository = get<DevicesRepositoryInterface>()) }
    viewModel<ListDeviceViewModel>(named("ListDeviceViewModel")) {
        ListDeviceViewModel(
            liveData = MutableLiveData(),
            dataSource = get<DevicesInterface>()
        )
    }
}