package com.oboringleb.androidcourse.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.oboringleb.androidcourse.BuildConfig
import com.oboringleb.androidcourse.util.logBackstack
import com.oboringleb.androidcourse.util.logFragmentHierarchy
import timber.log.Timber

open class BaseFragment: Fragment {
    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onStart() {
        super.onStart()
        if (BuildConfig.DEBUG) {
            val logTag = "NavigationInfo"
            logFragmentHierarchy(logTag)
            try {
                findNavController().logBackstack(logTag)
            } catch (error: IllegalStateException) {
                Timber.e(error)
            }
        }
    }
}