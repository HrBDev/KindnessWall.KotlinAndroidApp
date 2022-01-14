package ir.kindnesswall.view.main.more

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.ViewModel
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.UserPreferences
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.data.repository.UserRepo

class MoreViewModel(
    private val userRepo: UserRepo
):ViewModel() {
    @SuppressLint("CommitPrefEdits")
    fun NumberStatus(view: View) {
        if (UserInfoPref.bearerToken.length != 0)
            when (view.id) {
                ir.kindnesswall.R.id.more_none ->
                    UserPreferences.phoneVisibilityStatus = PhoneVisibility.None
                ir.kindnesswall.R.id.more_charity ->
                    UserPreferences.phoneVisibilityStatus = PhoneVisibility.JustCharities
                ir.kindnesswall.R.id.more_all ->
                    UserPreferences.phoneVisibilityStatus = PhoneVisibility.All
            }
    }
}