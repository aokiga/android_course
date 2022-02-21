package com.oboringleb.androidcourse

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.databinding.FragmentUserListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserListFragment: BaseFragment(R.layout.fragment_user_list) {
    private val viewModel: UserListViewModel by viewModels()

    private val viewBinding by viewBinding(FragmentUserListBinding::bind)

    companion object {
        val LOG_TAG = UserListFragment::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onCreate()")
        setupRecyclerView()
        subscribeToViewState()
    }

    private fun subscribeToViewState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    Log.d(LOG_TAG, viewState.toString())
                    renderViewState(viewState)
                }
            }
        }
    }

    private fun renderViewState(viewState: UserListViewModel.ViewState) {
        when (viewState) {
            is UserListViewModel.ViewState.Loading -> {
                viewBinding.usersRecycleView.isVisible = false
                viewBinding.progressBar.isVisible = true
            }
            is UserListViewModel.ViewState.Data -> {
                viewBinding.progressBar.isVisible = false
                viewBinding.usersRecycleView.isVisible = true
                (viewBinding.usersRecycleView.adapter as UserAdapter).apply {
                    userList = viewState.userList
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupRecyclerView(): UserAdapter {
        return UserAdapter().also {
            viewBinding.usersRecycleView.adapter = it
        }
    }
}