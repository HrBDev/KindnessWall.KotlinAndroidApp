package ir.kindnesswall.view.main.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.BuildConfig
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.local.UserPreferences
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.PhoneVisibility
import ir.kindnesswall.utils.extentions.runOrStartAuth
import ir.kindnesswall.utils.isAppAvailable
import ir.kindnesswall.view.main.MainActivity
import ir.kindnesswall.view.main.addproduct.SubmitGiftViewModel
import ir.kindnesswall.view.profile.UserProfileActivity
import ir.kindnesswall.view.profile.blocklist.BlockListActivity
import ir.kindnesswall.view.reviewgift.ReviewGiftsActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by farshid.abazari since 2019-11-07
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class MoreFragment() : BaseFragment() {
    var numview: Boolean = true
    private val viewModel: SubmitGiftViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }.also {
            MainActivity.liveData.observe(viewLifecycleOwner) {
                if (UserInfoPref.isGuestUser) return@observe
                viewModel.getPhoneVisibility()
                    .observe(viewLifecycleOwner) {
                        if (it.status == CustomResult.Status.SUCCESS)
                            UserPreferences.phoneVisibilityStatus = when (it.data?.setting) {
                                "all" -> PhoneVisibility.All
                                "none" -> PhoneVisibility.None
                                "charity" -> PhoneVisibility.JustCharities
                                else -> null
                            }
                    }
            }
        }
    }

    override fun configureViews() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view as ComposeView
        view.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        view.setContent {
            MdcTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = stringResource(R.string.more_items)) })
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Header(
                            name = UserInfoPref.name,
                            phoneNumber = UserInfoPref.getPersianPhoneNumber()
                        )

                        if (UserInfoPref.isGuestUser.not())
                            Item(
                                textResId = R.string.my_profile,
                                imgResId = R.drawable.ic_profile_placeholder_gary
                            ) {
                                UserProfileActivity.start(
                                    requireContext(),
                                    UserInfoPref.getUser()
                                )
                            }

                        if (UserInfoPref.isGuestUser.not())
                            Item(
                                textResId = R.string.show_number,
                                imgResId = R.drawable.ic_baseline_phone_iphone_24
                            ) { openPhoneVisibilityDialog() }

                        if (UserInfoPref.isAdmin)
                            Item(
                                textResId = R.string.check_submitted_gifts,
                                imgResId = R.drawable.ic_check_and_review
                            ) {
                                ReviewGiftsActivity.start(requireContext())
                            }

                        /*Item(
                            textResId = R.string.bookmarks,
                            imgResId = R.drawable.ic_bookmark_gray
                        ) {}*/

                        /*Item(
                            textResId = R.string.setting,
                            imgResId = R.drawable.ic_settings_gray
                        ) {}*/

                        if (UserInfoPref.isGuestUser.not())
                            Item(
                                textResId = R.string.blocked_users,
                                imgResId = R.drawable.ic_block_gray
                            ) { BlockListActivity.start(requireContext()) }

                        if (BuildConfig.DEBUG)
                            Item(
                                textResId = R.string.help_seekers_label,
                                imgResId = android.R.drawable.arrow_down_float/*TODO*/
                            ) { }

                        /*Item(
                            textResId = R.string.about,
                            imgResId = R.drawable.ic_info_gray
                        ) { AboutUsActivity.start(requireContext()) }*/

                        Item(
                            textResId = R.string.pros_and_cons,
                            imgResId = R.drawable.ic_pros_and_cons
                        ) { openTelegram() }

                        Item(
                            textResId = R.string.report_bug,
                            imgResId = R.drawable.ic_bug
                        ) { openTelegram() }

                        Item(
                            textResId = R.string.contact_us,
                            imgResId = R.drawable.ic_cotact_us
                        ) { openTelegram() }

                        if (UserInfoPref.isGuestUser)
                            Item(textResId = R.string.login, imgResId = R.drawable.ic_exit) { openAuth() }
                        else
                            Item(textResId = R.string.logout, imgResId = R.drawable.ic_exit) { openAuth() }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.version, BuildConfig.VERSION_NAME),
                            style = MaterialTheme.typography.overline,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }

    private fun openAuth() {
        context?.runOrStartAuth {
            showPromptDialog(
                getString(R.string.logout_message),
                positiveButtonText = getString(R.string.yes),
                negativeButtonText = getString(R.string.no),
                onPositiveClickCallback = {
                    UserInfoPref.clear()
                    AppPref.clear()
                    KindnessApplication.instance.clearContactList()
                    activity?.recreate()
                })
        }
    }

    private fun openTelegram() {
        context?.let {
            val packageName = "org.telegram.messenger"
            if (isAppAvailable(it, packageName)) {
                val telegramIntent = Intent(Intent.ACTION_VIEW)
                telegramIntent.data = Uri.parse("http://telegram.me/Kindness_Wall_Admin")
                startActivity(telegramIntent)
            } else {
                showToastMessage(getString(R.string.install_telegram))
            }
        }
    }

    private fun openPhoneVisibilityDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.show_number)
            .setSingleChoiceItems(
                arrayOf(
                    getString(R.string.none),
                    getString(R.string.charity),
                    getString(R.string.all),
                ),
                when (UserPreferences.phoneVisibilityStatus) {
                    PhoneVisibility.None -> 0
                    PhoneVisibility.JustCharities -> 1
                    PhoneVisibility.All -> 2
                    null -> -1
                },
                null
            )
            .setPositiveButton(
                R.string.accept
            ) { _, which ->
                val item = when (which) {
                    0 -> PhoneVisibility.None
                    1 -> PhoneVisibility.JustCharities
                    2 -> PhoneVisibility.All
                    else -> error("unknown item. $which")
                }
                UserPreferences.phoneVisibilityStatus = item
                viewModel.setPhoneVisibility(item).observe(viewLifecycleOwner) {}
            }
            .setNeutralButton(R.string.close_modal,null)
            .show()
    }
}

@Composable
private fun Header(name: String, phoneNumber: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.ic_profile_placeholder_gary),
            contentDescription = "Avatar",
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name.ifBlank { stringResource(R.string.user_name_place_holder) },
            style = MaterialTheme.typography.h6
        )

        Text(
            text = phoneNumber,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun Item(textResId: Int, imgResId: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(imgResId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = colorResource(R.color.secondaryTextColor)),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(textResId),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1
        )
    }
}