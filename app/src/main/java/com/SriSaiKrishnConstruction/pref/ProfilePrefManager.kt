package com.SriSaiKrishnConstruction.pref

import android.content.Context
import android.content.SharedPreferences

class ProfilePrefManager private constructor(context: Context) {

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(sharedPreferences.getString(KEY_NAME, null),
                    sharedPreferences.getString(KEY_ADDRESS, null),
                    sharedPreferences.getString(KEY_DESC, null),
                    sharedPreferences.getString(KEY_SUPERVISOR, null),
                    sharedPreferences.getString(KEY_DESC, null))
        }

    init {
        mCtx = context
    }

    fun userProfile(profileUser: ProfileUser) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, profileUser.s_name)
        editor.putString(KEY_ADDRESS, profileUser.address)
        editor.putString(KEY_DESC, profileUser.description)
        editor.putString(KEY_SUPERVISOR, profileUser.supervisor_name)
        editor.putString(KEY_DESC, profileUser.description)
        editor.apply()
    }

    companion object {

        private val SHARED_PREF_NAME = "Profilesinghpref"
        private val KEY_NAME = "s_name"
        private val KEY_ADDRESS = "address"
        private val KEY_DESC = "description"
        private val KEY_SUPERVISOR = "supervisor_name"

        private  var mInstance: ProfilePrefManager? = null
        private lateinit var mCtx: Context

        @Synchronized
        fun getInstance(context: Context): ProfilePrefManager {
            if (mInstance == null) {
                mInstance = ProfilePrefManager(context)
            }
            return mInstance as ProfilePrefManager
        }
    }

}