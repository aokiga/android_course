package com.oboringleb.androidcourse.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.FragmentOnboardingBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {
    private val viewBinding by viewBinding(FragmentOnboardingBinding::bind)

    private var player: ExoPlayer? = null

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = SimpleExoPlayer.Builder(requireContext()).build().apply {
            addMediaItem(MediaItem.fromUri("asset:///onboarding.mp4"))
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
            volume = 0f
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.playerView.player = player

        viewBinding.viewPager.setTextPages()
        viewBinding.viewPager.attachDots(viewBinding.onboardingTextTabLayout)

        viewBinding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
        }
        viewBinding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signUpFragment)
        }

        subscribeToVideoSoundState()
        subscribeToChangeVideoSoundEvents()

        subscribeToAutoscrollState()
        subscribeOnAutoscrollEvents()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    private fun ViewPager2.setTextPages() {
        adapter =
            ListDelegationAdapter(onboardingTextAdapterDelegate()).apply {
                items =
                    listOf(
                        getString(R.string.onboarding_view_pager_text_1),
                        getString(R.string.onboarding_view_pager_text_2),
                        getString(R.string.onboarding_view_pager_text_3)
                    )
            }
    }

    private fun ViewPager2.attachDots(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, this) { _, _ -> }.attach()
    }

    private fun changeVolumeIconResource(volumeFlag: Boolean) {
        if (volumeFlag) {
            viewBinding.volumeControlButton.setImageResource(R.drawable.ic_volume_up_white_24dp)
        } else {
            viewBinding.volumeControlButton.setImageResource(R.drawable.ic_volume_off_white_24dp)
        }
    }

    private fun subscribeToVideoSoundState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.videoSoundState.collect { videoSoundState ->
                    val newVolume: Float = if (videoSoundState) 1f else 0f
                    player?.volume = newVolume
                    changeVolumeIconResource(videoSoundState)
                }
            }
        }
    }

    private fun subscribeToChangeVideoSoundEvents() {
        viewBinding.volumeControlButton.setOnClickListener {
            viewModel.changeVideoSoundState()
        }
    }

    private fun subscribeToAutoscrollState() {
        lifecycleScope.launch {
            launch {
                val initialIndex = viewBinding.viewPager.currentItem
                val itemsCount = viewBinding.viewPager.adapter!!.itemCount
                viewModel.launchAutoScroll(initialIndex, itemsCount)
            }
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.scrollIndexFlow.collect { index ->
                        val currentIndex = viewBinding.viewPager.currentItem
                        if (currentIndex != index) {
                            viewBinding.viewPager.setCurrentItem(index, true)
                        }
                    }
                }
            }
        }
    }


    private fun subscribeOnAutoscrollEvents() {
        viewBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.registerScrollEvent(position)
            }
        })
    }
}