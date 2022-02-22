package com.oboringleb.androidcourse.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentSignUpBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val viewBinding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.signUpButton.setOnClickListener {
            viewModel.signUp()
        }
    }
}