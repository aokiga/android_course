package com.oboringleb.androidcourse.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.R
import com.oboringleb.androidcourse.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        subscribeToAuthorizationStatus()
    }

    private fun subscribeToAuthorizationStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuthorizedFlow.collect {
                    showSuitableNavigationFlow(it)
                }
            }
        }
    }

    // This method have to be idempotent. Do not override restored backstack.
    private fun showSuitableNavigationFlow(isAuthorized: Boolean) {
        val navController = findNavController(R.id.mainActivityNavigationHost)
        when (isAuthorized) {
            true -> {
                if (navController.backQueue.any { it.destination.id == R.id.registered_user_navigation_graph}) {
                    return
                }
                navController.navigate(R.id.action_registeredUserNavigationGraph)
            }
            false -> {
                if (navController.backQueue.any { it.destination.id == R.id.guest_navigation_graph}) {
                    return
                }
                navController.navigate(R.id.action_guestNavigationGraph)
            }
        }
    }
}