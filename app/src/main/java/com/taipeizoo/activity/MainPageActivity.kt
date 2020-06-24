package com.taipeizoo.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.taipeizoo.R
import com.taipeizoo.databinding.ActivityMainBinding
import com.taipeizoo.fragment.AnimalAreaDetailFragment
import com.taipeizoo.fragment.MainPageFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainPageActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        binding.lifecycleOwner = this
        showProgress(true)
        navigateTo(MainPageFragment(), false)
    }

    fun showProgress(show: Boolean) {
        if (show) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressBar.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.visibility = View.GONE
        }
    }

    fun onClickOpenAnimalInfoInBrowser(view: View) {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is AnimalAreaDetailFragment) {
            ((fragment as AnimalAreaDetailFragment).openAnimalInfoInBrowser())
        }
    }
}