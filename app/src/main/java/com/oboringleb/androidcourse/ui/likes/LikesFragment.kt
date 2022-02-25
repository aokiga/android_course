package com.oboringleb.androidcourse.ui.likes

import androidx.fragment.app.viewModels
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentLikesBinding

class LikesFragment: BaseFragment(R.layout.fragment_likes) {
    private val viewBinding by viewBinding(FragmentLikesBinding::bind)

    private val viewModel: LikesViewModel by viewModels()
}