package ir.kindnesswall.view.main.more

import androidx.lifecycle.ViewModel
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.UserPreferences
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.data.repository.UserRepo

class MoreViewModel(
    private val userRepo: UserRepo
) : ViewModel() {
    fun setPhoneVisibilityState(visibility: PhoneVisibility) {
        if (UserInfoPref.isGuestUser) return

        UserPreferences.phoneVisibilityStatus = visibility
    }
}