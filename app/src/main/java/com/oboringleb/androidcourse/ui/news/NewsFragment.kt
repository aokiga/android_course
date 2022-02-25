package com.oboringleb.androidcourse.ui.news

import androidx.fragment.app.viewModels
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentNewsBinding
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding

class NewsFragment: BaseFragment(R.layout.fragment_news) {
    private val viewBinding by viewBinding(FragmentNewsBinding::bind)

    private val viewModel: NewsViewModel by viewModels()
}