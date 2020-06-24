package com.taipeizoo.activity

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

interface NavigationHost {
    fun navigateTo(fragment: Fragment, addToBackStack: Boolean)
    fun launchActivity(activity: Activity)
    fun launchActivity(activity: Activity, intent: Intent)
    fun launchActivityForResult(activity: Activity, requestCode: Int)
    fun launchActivityForResult(requestCode: Int, intent: Intent)
}