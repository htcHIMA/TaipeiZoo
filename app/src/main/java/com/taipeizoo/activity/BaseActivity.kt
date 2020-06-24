package com.taipeizoo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.taipeizoo.R

open class BaseActivity : AppCompatActivity(), NavigationHost {
    override fun navigateTo(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun launchActivity(activity: Activity) {
        val intent = Intent(this, activity.javaClass)
        startActivity(intent)
    }

    override fun launchActivity(activity: Activity, intent: Intent) {
        startActivity(intent)
    }

    override fun launchActivityForResult(activity: Activity, requestCode: Int) {
        val intent = Intent(this, activity.javaClass)
        startActivityForResult(intent, requestCode)
    }

    override fun launchActivityForResult(requestCode: Int, intent: Intent) {
        startActivityForResult(intent, requestCode)
    }
}