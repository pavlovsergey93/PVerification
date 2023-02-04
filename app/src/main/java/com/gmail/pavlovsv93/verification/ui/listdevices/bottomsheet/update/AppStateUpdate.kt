package com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.update

sealed interface AppStateUpdate {
    data class OnShowMessage(val message: String) : AppStateUpdate
}