package com.SriSaiKrishnConstruction.pref

import android.content.Context
import android.content.Intent
import com.SriSaiKrishnConstruction.main.Login

class SharedPrefManager private constructor(context: Context) {
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_USERNAME, null) != null
        }
    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(sharedPreferences.getString(KEY_ID, null),
                    sharedPreferences.getString(KEY_MOBILE, null),
                    sharedPreferences.getString(KEY_BRANCH_ID, null),
                    sharedPreferences.getString(KEY_USERNAME, null),
                    sharedPreferences.getString(KEY_EMAIL, null))
        }

    init {
        mCtx = context
    }
    fun userLogin(user: User) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ID, user.id)
        editor.putString(KEY_USERNAME, user.username)
        editor.putString(KEY_BRANCH_ID, user.branch_id)
        editor.putString(KEY_EMAIL, user.email)
        editor.apply()
    }

    fun userDrawer(user: User) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, user.username)
        editor.putString(KEY_BRANCH_ID, user.branch_id)
        editor.apply()
    }

    fun logout() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        mCtx.startActivity(Intent(mCtx, Login::class.java))
    }

    companion object {
        private val SHARED_PREF_NAME = "SriSaiKrishnapref"
        private val KEY_USERNAME = "keyusername"
        private val KEY_BRANCH_ID = "branch_id"
        private val KEY_EMAIL = "email"
        private val KEY_MOBILE = "mobile"
        private val KEY_ID = "keyid"

        private var mInstance: SharedPrefManager? = null
        private lateinit var mCtx: Context
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }
}
