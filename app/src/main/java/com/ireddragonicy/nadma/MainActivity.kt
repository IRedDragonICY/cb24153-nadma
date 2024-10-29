package com.ireddragonicy.nadma

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragments = mutableMapOf<Int, Fragment>()
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    val currentItem = bottomNav.selectedItemId
                    if (item.itemId > currentItem) {
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
