package com.oboringleb.androidcourse.ui.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentSignInBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding

class SignInFragment: BaseFragment(R.layout.fragment_sign_in) {
    private val viewBinding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.signInButton.setOnClickListener {
            viewModel.signIn()
        }
    }
}