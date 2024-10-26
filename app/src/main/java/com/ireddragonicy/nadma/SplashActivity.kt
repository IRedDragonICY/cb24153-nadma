package com.ireddragonicy.nadma

import android.annotation.SuppressLint
import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            navigateToNextScreen()
        }
    }

    private fun navigateToNextScreen() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)

        if (isFirstTime) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_TIME, false).apply()
            startActivity(Intent(this, WelcomeActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}