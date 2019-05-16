package com.beyondedge.hm.base

import android.os.Bundle
import androidx.fragment.app.Fragment

import com.beyondedge.hm.R

/**
 * Created by Hoa Nguyen on Apr 24 2019.
 */
abstract class BaseActivitySingleFragment : BaseTemplateActivity() {
    lateinit var currentFragment: Fragment
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_single_fragment)
        initSearchView()
        currentFragment = createFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.frame_content, currentFragment, "Single_Fragment")
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .commit()
    }

    protected abstract fun createFragment(): Fragment
}
