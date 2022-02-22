package com.oboringleb.androidcourse.ui.emailconfirmation

import androidx.fragment.app.viewModels
import com.oboringleb.androidcourse.databinding.FragmentEmailConfirmationBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.R

class EmailConfirmationFragment: BaseFragment(R.layout.fragment_email_confirmation) {
    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()
}