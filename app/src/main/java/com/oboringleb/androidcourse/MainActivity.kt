package com.oboringleb.androidcourse

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.oboringleb.androidcourse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        val LOG_TAG = MainActivity::class.java.simpleName
    }

    private val viewModel: MainViewModel by viewModels()

    private val viewBinding by viewBinding(ActivityMainBinding::bind)
}