package com.academica.tenant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.academica.tenant.databinding.ActivityMainBinding
import com.academica.tenant.menu.AkunFragment
import com.academica.tenant.menu.HomeFragment

import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var menu: Menu = binding.bottomView.menu
        selectedMenu(menu.getItem(0))
        binding.bottomView.setOnNavigationItemSelectedListener { item: MenuItem ->
            selectedMenu(item)
            false
        }
        binding.bottomView.textAlignment = View.TEXT_ALIGNMENT_CENTER

    }

    private fun selectedMenu(item: MenuItem) {
        item.isChecked = true
        when (item.itemId) {
            R.id.nav_home  -> setCurrentFragment(HomeFragment())
            R.id.nav_akun  -> setCurrentFragment(AkunFragment())
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.rootFragment, fragment).commit()
        }




}