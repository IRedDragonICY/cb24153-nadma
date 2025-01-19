package com.ireddragonicy.nadma

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val sessionManager by lazy { SessionManager(this) }

    private val navOrder = listOf(
        R.id.nav_home,
        R.id.nav_maps,
        R.id.nav_news,
        R.id.nav_notification,
        R.id.nav_account
    )

    private val fragments = mutableMapOf<Int, Fragment>()
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (!sessionManager.isLoggedIn) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val homeFragment = HomeFragment()
        fragments[R.id.nav_home] = homeFragment
        activeFragment = homeFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, homeFragment, R.id.nav_home.toString())
            .commit()

        bottomNav.setOnItemSelectedListener { item ->
            val targetFragment = getFragment(item.itemId)
            if (targetFragment != null && targetFragment != activeFragment) {
                supportFragmentManager.beginTransaction().apply {
                    val currentItemId = bottomNav.selectedItemId
                    val currentPos = navOrder.indexOf(currentItemId)
                    val targetPos = navOrder.indexOf(item.itemId)

                    if (targetPos > currentPos) {
                        setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                    } else {
                        setCustomAnimations(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right,
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                    }

                    hide(activeFragment!!)
                    show(targetFragment)
                    commit()
                }
                activeFragment = targetFragment
                true
            } else {
                false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }
    }

    private fun getFragment(itemId: Int): Fragment? {
        return fragments[itemId] ?: createFragment(itemId)?.also { fragment ->
            fragments[itemId] = fragment
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .add(R.id.main_container, fragment, itemId.toString())
                .hide(fragment)
                .commit()
        }
    }

    private fun createFragment(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.nav_maps -> MapsFragment()
            R.id.nav_news -> NewsFragment()
            R.id.nav_notification -> NotificationFragment()
            R.id.nav_account -> AccountFragment()
            else -> null
        }
    }
}