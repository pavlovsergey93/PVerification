package com.gmail.pavlovsv93.verification.di

import androidx.lifecycle.MutableLiveData
import com.gmail.pavlovsv93.verification.data.RemoteDataSource
import com.gmail.pavlovsv93.verification.data.realtime.RealtimeRepository
import com.gmail.pavlovsv93.verification.data.realtime.RealtimeRepositoryInterface
import com.gmail.pavlovsv93.verification.data.repository.RemoteRepository
import com.gmail.pavlovsv93.verification.data.repository.RepositoryInterface
import com.gmail.pavlovsv93.verification.domain.DataSourceInterface
import com.gmail.pavlovsv93.verification.ui.addfragment.AddOrUpdateKipEntityViewModel
import com.gmail.pavlovsv93.verification.ui.detailsdevice.DetailsViewModel
import com.gmail.pavlovsv93.verification.ui.listdevices.ListDeviceViewModel
import com.gmail.pavlovsv93.verification.ui.remove.RemoveInVerificationViewModel
import com.gmail.pavlovsv93.verification.ui.waitreturn.WaitingForReturnViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
	single<FirebaseDatabase> { FirebaseDatabase.getInstance()	}
	single<DatabaseReference>{ get<FirebaseDatabase>().reference }
	single<RealtimeRepositoryInterface> {
		RealtimeRepository(
			reference = get()
		)
	}
	single { FirebaseFirestore.getInstance() }
	single<RepositoryInterface> { RemoteRepository(db = get<FirebaseFirestore>()) }
	single<DataSourceInterface> { RemoteDataSource(repository = get<RepositoryInterface>()) }
	viewModel<ListDeviceViewModel>(named("ListDeviceViewModel")) {
		ListDeviceViewModel(
			liveData = MutableLiveData(),
			dataSource = get<DataSourceInterface>()
		)
	}
	viewModel<DetailsViewModel>(named("DetailsViewModel")) {
		DetailsViewModel(
			liveData = MutableLiveData(),
			dataSource = get<DataSourceInterface>()
		)
	}
	viewModel<AddOrUpdateKipEntityViewModel>(named("AddOrUpdateKipEntityViewModel")) {
		AddOrUpdateKipEntityViewModel(
			liveData = MutableLiveData(),
			dataSource = get<DataSourceInterface>()
		)
	}
	viewModel<RemoveInVerificationViewModel>(named("RemoveInVerificationViewModel")) {
		RemoveInVerificationViewModel(
			liveData = MutableLiveData(),
			dataSource = get<DataSourceInterface>(),
			repository = get()
		)
	}
	viewModel<WaitingForReturnViewModel>(named("WaitingForReturnViewModel")) {
		WaitingForReturnViewModel(
			liveData = MutableLiveData(),
			dataSource = get<DataSourceInterface>(),
			repository = get<RealtimeRepositoryInterface>()
		)
	}
}