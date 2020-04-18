package com.elouyi.bbs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.elouyi.bbs.fragment.SettingsFragment
import com.elouyi.bbs.t.getUserFromShare
import com.elouyi.bbs.t.logOut
import com.elouyi.elylib.ActivityCollector
import com.elouyi.elylib.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingsActivity : ElyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        title = "设置    -- " + getUserFromShare(this).username
        replaceFragment(SettingsFragment())
        initv()
    }

    private fun initv(){
        btnLogout.setOnClickListener {
            logOut(this)
            ActivityCollector.finishAll()
            startActivity<MainActivity>(this){}
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.settingsFrameLayout,fragment)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
