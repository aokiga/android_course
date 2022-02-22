package com.oboringleb.androidcourse.ui.profile

import androidx.fragment.app.viewModels
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.databinding.FragmentProfileBinding
import com.oboringleb.androidcourse.ui.news.NewsViewModel

class ProfileFragment: BaseFragment(R.layout.fragment_profile) {
    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: NewsViewModel by viewModels()
}