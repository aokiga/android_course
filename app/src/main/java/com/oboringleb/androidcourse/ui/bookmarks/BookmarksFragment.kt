package com.oboringleb.androidcourse.ui.bookmarks

import androidx.fragment.app.viewModels
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.databinding.FragmentBookmarksBinding

class BookmarksFragment: BaseFragment(R.layout.fragment_bookmarks) {

    private val viewBinding by viewBinding(FragmentBookmarksBinding::bind)

    private val viewModel: BookmarksViewModel by viewModels()
}