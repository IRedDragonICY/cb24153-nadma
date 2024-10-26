package com.ireddragonicy.nadma

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    companion object {
        private const val SPLASH_DELAY = 2000L
        private const val PREFS_NAME = "AppPreferences"
        private const val KEY_FIRST_TIME = "isFirstTime"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        val isFirstTime = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)
        val nextActivity = if (isFirstTime) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_TIME, false).apply()
            WelcomeActivity::class.java
        } else {
            MainActivity::class.java
        }

        startActivity(Intent(this, nextActivity).also {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out)
        })
        finish()
    }
}
