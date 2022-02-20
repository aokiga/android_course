package com.oboringleb.androidcourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    companion object {
        val LOG_TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate()")
        setupRecyclerView()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { viewState ->
                    Log.d(LOG_TAG, viewState.toString())
                    renderViewState(viewState)
                }
            }
        }
    }

    private fun renderViewState(viewState: MainViewModel.ViewState) {
        when (viewState) {
            is MainViewModel.ViewState.Loading -> {
                viewBinding.usersRecycleView.isVisible = false
                viewBinding.progressBar.isVisible = true
            }
            is MainViewModel.ViewState.Data -> {
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
        val recycleView = findViewById<RecyclerView>(R.id.usersRecycleView)
        recycleView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = UserAdapter()
        recycleView.adapter = adapter
        return adapter
    }
}