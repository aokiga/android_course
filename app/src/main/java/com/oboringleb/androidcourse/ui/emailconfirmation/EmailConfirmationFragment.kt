package com.oboringleb.androidcourse.ui.emailconfirmation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.oboringleb.androidcourse.databinding.FragmentEmailConfirmationBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.util.getSpannedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.confirmButton.setOnClickListener {
            viewModel.confirmCode("","","","","", "") // TODO()
        }
        viewBinding.sendCodeAgainButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.askNewCode()
            }
        }
        subscribeToTimeNotifications()
    }

    private fun subscribeToTimeNotifications() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    if (viewModel.timeLeftFlow.value == 0L) {
                        viewBinding.sendCodeAgainHintText.visibility = TextView.INVISIBLE
                        viewBinding.sendCodeAgainButton.isEnabled = true
                    } else {
                        viewBinding.sendCodeAgainHintText.visibility = TextView.VISIBLE
                        viewBinding.sendCodeAgainButton.isEnabled = false
                        viewBinding.sendCodeAgainHintText.setSendCodeTimerHint(viewModel.timeLeftFlow.value)
                    }
                    delay(900L)
                }
            }
        }
    }

    private fun TextView.setSendCodeTimerHint(seconds: Long) {
        text = resources.getSpannedString(
            R.string.email_confirmation_send_code_again_hint_template,
            buildSpannedString {
                inSpans {
                    append("%02d:%02d".format(seconds / 60, seconds % 60))
                }
            }
        )
    }
}