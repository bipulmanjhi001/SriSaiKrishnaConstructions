package com.SriSaiKrishnConstruction.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.SriSaiKrishnConstruction.R
import com.SriSaiKrishnConstruction.fragments.AddTrip
import com.SriSaiKrishnConstruction.fragments.Home
import com.SriSaiKrishnConstruction.fragments.Profile
import com.SriSaiKrishnConstruction.fragments.Trips

class TripActivity : AppCompatActivity() {

    internal var fragment: Fragment? = null
    internal lateinit var title: TextView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_home -> {
                fragment = Trips()
                loadFragment(fragment)
                title.text = "Trip"
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_dashboard -> {
                fragment = AddTrip()
                loadFragment(fragment)
                title.text = "Add Trip"
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_notifications -> {
                fragment = Profile()
                loadFragment(fragment)
                title.text = "Profile"
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.trip, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = findViewById<TextView>(R.id.title)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.trip, Trips()).commit()
        title.text = "Trip"

    }
}
