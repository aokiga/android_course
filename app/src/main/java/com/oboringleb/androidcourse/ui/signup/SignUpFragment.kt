package com.oboringleb.androidcourse.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentSignUpBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.util.setClubRulesText
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val viewModel: SignUpViewModel by viewModels()
    private val viewBinding by viewBinding(FragmentSignUpBinding::bind)

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
        viewBinding.backButton.setOnClickListener {
            onBackButtonPressed()
        }
        viewBinding.signUpButton.setOnClickListener {
            viewModel.signUp(
                firstname = viewBinding.firstnameEditText.text?.toString() ?: "",
                lastname = viewBinding.lastnameEditText.text?.toString() ?: "",
                nickname = viewBinding.nicknameEditText.text?.toString() ?: "",
                email = viewBinding.emailEditText.text?.toString() ?: "",
                password = viewBinding.passwordEditText.text?.toString() ?: ""
            )
        }
        subscribeToEvents()
        subscribeToFormFields()
        viewBinding.termsAndConditionsCheckBox.setClubRulesText {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://policies.google.com/terms")))
        }
    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        is SignUpViewModel.Event.SignUpEmailConfirmationRequired -> {
                            findNavController().navigate(R.id.action_signUpFragment_to_emailConfirmationFragment)
                        }
                        else -> {
                            // Do nothing.
                        }
                    }
                }
            }
        }
    }

    private fun subscribeToFormFields() {
        decideSignUpButtonEnabledState(
            firstname = viewBinding.firstnameEditText.text?.toString(),
            lastname = viewBinding.lastnameEditText.text?.toString(),
            nickname = viewBinding.nicknameEditText.text?.toString(),
            email = viewBinding.emailEditText.text?.toString(),
            password = viewBinding.passwordEditText.text?.toString(),
            termsIsChecked = viewBinding.termsAndConditionsCheckBox.isChecked
        )
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                decideSignUpButtonEnabledState(
                    firstname = viewBinding.firstnameEditText.text?.toString(),
                    lastname = viewBinding.lastnameEditText.text?.toString(),
                    nickname = viewBinding.nicknameEditText.text?.toString(),
                    email = viewBinding.emailEditText.text?.toString(),
                    password = viewBinding.passwordEditText.text?.toString(),
                    termsIsChecked = viewBinding.termsAndConditionsCheckBox.isChecked
                )
            }

        }
        viewBinding.firstnameEditText.addTextChangedListener(watcher)
        viewBinding.lastnameEditText.addTextChangedListener(watcher)
        viewBinding.nicknameEditText.addTextChangedListener(watcher)
        viewBinding.emailEditText.addTextChangedListener(watcher)
        viewBinding.passwordEditText.addTextChangedListener(watcher)
        viewBinding.termsAndConditionsCheckBox.setOnClickListener {
            decideSignUpButtonEnabledState(
                firstname = viewBinding.firstnameEditText.text?.toString(),
                lastname = viewBinding.lastnameEditText.text?.toString(),
                nickname = viewBinding.nicknameEditText.text?.toString(),
                email = viewBinding.emailEditText.text?.toString(),
                password = viewBinding.passwordEditText.text?.toString(),
                termsIsChecked = viewBinding.termsAndConditionsCheckBox.isChecked
            )
        }
    }

    private fun decideSignUpButtonEnabledState(
        firstname: String?,
        lastname: String?,
        nickname: String?,
        email: String?,
        password: String?,
        termsIsChecked: Boolean
    ) {
        viewBinding.signUpButton.isEnabled = !firstname.isNullOrBlank()
                && !lastname.isNullOrBlank()
                && !nickname.isNullOrBlank()
                && !email.isNullOrBlank()
                && !password.isNullOrBlank()
                && termsIsChecked
    }

    private fun onBackButtonPressed() {
        val email = viewBinding.emailEditText.text?.toString()
        val password = viewBinding.passwordEditText.text?.toString()
        if (email.isNullOrBlank() && password.isNullOrBlank()) {
            findNavController().popBackStack()
            return
        }
        android.app.AlertDialog.Builder(requireContext())
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