package com.oboringleb.androidcourse.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.ui.base.BaseFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.databinding.FragmentProfileBinding
import com.oboringleb.androidcourse.ui.userlist.UserAdapter
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileFragment: BaseFragment(R.layout.fragment_profile) {
    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnEmojiCountersUpdates()
        subscribeToViewState()
        viewBinding.smileyHeartButton.setOnClickListener { viewModel.inc(viewModel.heart) }
        viewBinding.smileyPooButton.setOnClickListener { viewModel.inc(viewModel.poo) }
        viewBinding.smileyLaughButton.setOnClickListener { viewModel.inc(viewModel.laugh) }
        lifecycleScope.launch {
            viewModel.loadProfile()
            viewModel.viewState.collect {
                if (it !is ProfileViewModel.ViewState.Data) return@collect
                viewBinding.nameText.text = it.profile.userName
                viewBinding.groupText.text = it.profile.groupName
                viewBinding.userImage.setImageBitmap(viewModel.image.value)
            }
        }
        viewBinding.logoutButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun subscribeToViewState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    renderViewState(viewState)
                }
            }
        }
    }

    private fun renderViewState(viewState: ProfileViewModel.ViewState) {
        when (viewState) {
            is ProfileViewModel.ViewState.Loading -> {
                viewBinding.groupText.isVisible = false
                viewBinding.nameText.isVisible = false
                viewBinding.userImage.isVisible = false
                viewBinding.progressBar.isVisible = true
            }
            is ProfileViewModel.ViewState.Data -> {
                viewBinding.progressBar.isVisible = false
                viewBinding.groupText.isVisible = true
                viewBinding.nameText.isVisible = true
                viewBinding.userImage.isVisible = true
            }
        }
    }

    private fun subscribeOnEmojiCountersUpdates() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.heart.get().collect { viewBinding.heartCounter.text = it.toString() }
                delay(50L)
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.poo.get().collect { viewBinding.pooCounter.text = it.toString() }
                delay(50L)
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.laugh.get().collect { viewBinding.laughCounter.text = it.toString() }
                delay(50L)
            }
        }
    }


}