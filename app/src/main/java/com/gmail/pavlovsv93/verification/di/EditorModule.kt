package com.gmail.pavlovsv93.verification.di

import androidx.lifecycle.MutableLiveData
import com.gmail.pavlovsv93.verification.data.editor.EditorDataSource
import com.gmail.pavlovsv93.verification.data.editor.EditorRepository
import com.gmail.pavlovsv93.verification.data.editor.EditorRepositoryInterface
import com.gmail.pavlovsv93.verification.domain.datasource.EditorInterface
import com.gmail.pavlovsv93.verification.ui.addfragment.AddOrUpdateKipEntityViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val editorModule = module {
    single<EditorRepositoryInterface> { EditorRepository(db = get<FirebaseFirestore>()) }
    single<EditorInterface> { EditorDataSource(repository = get<EditorRepositoryInterface>()) }
    viewModel<AddOrUpdateKipEntityViewModel>(named("AddOrUpdateKipEntityViewModel")) {
        AddOrUpdateKipEntityViewModel(
            liveData = MutableLiveData(),
            dataSource = get<EditorInterface>()
        )
    }
}