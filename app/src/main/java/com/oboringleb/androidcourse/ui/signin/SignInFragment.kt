package com.oboringleb.androidcourse.ui.signin

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentSignInBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.chrisbanes.insetter.applyInsetter

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewBinding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackButtonPressed()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.backButton.setOnClickListener {
            onBackButtonPressed()
        }
        viewBinding.signInButton.setOnClickListener {
            viewModel.signIn(
                email = viewBinding.emailEditText.text?.toString() ?: "",
                password = viewBinding.passwordEditText.text?.toString() ?: ""
            )
        }
        viewBinding.signInButton.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }
        subscribeToFormFields()
        createLogoAnimation()
    }

    private fun createLogoAnimation() {
        viewBinding.mknLogoImageView.apply {
            val rotation = RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotation.interpolator = LinearInterpolator()
            rotation.duration = 1000L
            rotation.repeatCount = Animation.INFINITE
            startAnimation(rotation)
        }
    }

    private fun subscribeToFormFields() {
        decideSignInButtonEnabledState(
            email = viewBinding.emailEditText.text?.toString(),
            password = viewBinding.passwordEditText.text?.toString()
        )
        viewBinding.emailEditText.doAfterTextChanged { email ->
            decideSignInButtonEnabledState(
                email = email?.toString(),
                password = viewBinding.passwordEditText.text?.toString()
            )
        }
        viewBinding.passwordEditText.doAfterTextChanged { password ->
            decideSignInButtonEnabledState(
                email = viewBinding.emailEditText.text?.toString(),
                password = password?.toString()
            )
        }
    }

    private fun decideSignInButtonEnabledState(email: String?, password: String?) {
        viewBinding.signInButton.isEnabled = !(email.isNullOrBlank() || password.isNullOrBlank())
    }

    private fun onBackButtonPressed() {
        val email = viewBinding.emailEditText.text?.toString()
        val password = viewBinding.passwordEditText.text?.toString()
        if (email.isNullOrBlank() && password.isNullOrBlank()) {
            findNavController().popBackStack()
            return
        }
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.common_sign_back_alert_dialog_text)
            .setNegativeButton(R.string.common_sign_back_alert_dialog_cancel_button_text) { dialog, _ ->
                dialog?.dismiss()
            }
            .setPositiveButton(R.string.common_sign_back_alert_dialog_ok_button_text) { _, _ ->
                findNavController().popBackStack()
            }
            .show()
    }
}