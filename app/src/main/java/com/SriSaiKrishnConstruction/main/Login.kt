package com.SriSaiKrishnConstruction.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.SriSaiKrishnConstruction.R
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.SriSaiKrishnConstruction.api.URLs
import com.SriSaiKrishnConstruction.model.ConnectivityReceiver
import com.SriSaiKrishnConstruction.model.VolleySingleton
import com.SriSaiKrishnConstruction.pref.PrefManager
import com.SriSaiKrishnConstruction.pref.ProfilePrefManager
import com.SriSaiKrishnConstruction.pref.ProfileUser
import com.SriSaiKrishnConstruction.pref.SharedPrefManager
import com.SriSaiKrishnConstruction.pref.User
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Login : AppCompatActivity() {

    private var UserView: EditText? = null
    private var mPasswordView: EditText? = null
    internal lateinit var username: String
    internal lateinit var password: String

    private var prefManager: PrefManager? = null
    internal var tokens: String? = null
    internal var branch_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        UserView = findViewById<View>(R.id.user_id) as EditText
        mPasswordView = findViewById<View>(R.id.password) as EditText

        val prefs = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        tokens = prefs.getString(KEY_ID, null)
        branch_id = prefs.getString(KEY_BRANCH_ID, null)

        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            try {
                prefManager = PrefManager(this@Login)
                if (branch_id == "1") {
                    finish()
                    startActivity(Intent(this, StockActivity::class.java))

                } else if (branch_id == "2") {
                    finish()
                    startActivity(Intent(this, StockActivity::class.java))

                } else if (branch_id == "3") {
                    finish()
                    startActivity(Intent(this, StockActivity::class.java))
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

        }
        mPasswordView!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        val SignInButton = findViewById<View>(R.id.sign_in_button) as Button
        SignInButton.setOnClickListener { checkConnection() }
    }

    private fun checkConnection() {
        val isConnected = ConnectivityReceiver.isConnected
        showSnack(isConnected)
    }

    private fun showSnack(isConnected: Boolean) {
        val message: String
        val color: Int
        if (isConnected) {
            attemptLogin()
        } else {
            message = "connect your internet."
            color = Color.RED
            val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
            toast.show()
            finish()
        }
    }

    private fun attemptLogin() {
        UserView!!.error = null
        mPasswordView!!.error = null

        username = UserView!!.text.toString()
        password = mPasswordView!!.text.toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(username)) {
            UserView!!.error = getString(R.string.error_field_required)
            focusView = UserView
            cancel = true
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView!!.error = getString(R.string.error_incorrect_password)
            focusView = mPasswordView
            cancel = true
        }
        if (cancel) {
            focusView!!.requestFocus()
        } else {

            if (UserView!!.text.toString() == "admin" && mPasswordView!!.text.toString() == "12345") {

                val intent = Intent(applicationContext, StockActivity::class.java)
                startActivity(intent)
                finish()

            }
            // Authenticate();
        }
    }

    fun Authenticate() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getBoolean("status")) {

                            val userJson = obj.getJSONObject("user")
                            val token = userJson.getString("token")
                            val name = userJson.getString("name")
                            val branch_id = userJson.getString("branch_id")
                            val mobile = userJson.getString("mobile")
                            val email = userJson.getString("email")
                            val sites = userJson.getJSONObject("site")

                            val profileUser = ProfileUser(
                                    sites.getString("name"),
                                    sites.getString("address"),
                                    sites.getString("description"),
                                    sites.getString("supervisor_name")
                            )

                            val user = User(
                                    userJson.getString("token"),
                                    userJson.getString("mobile"),
                                    userJson.getString("branch_id"),
                                    userJson.getString("name"),
                                    userJson.getString("email")
                            )
                            SharedPrefManager.getInstance(applicationContext).userLogin(user)
                            finish()

                            ProfilePrefManager.getInstance(applicationContext).userProfile(profileUser)
                            finish()
                            if (branch_id == "1") {
                                val intent = Intent(applicationContext, StockActivity::class.java)
                                intent.putExtra("name", name)
                                intent.putExtra("token", token)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            }

                            if (branch_id == "2") {
                                val intent = Intent(applicationContext, StockActivity::class.java)
                                intent.putExtra("name", name)
                                intent.putExtra("token", token)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            }
                            if (branch_id == "3") {
                                val intent = Intent(applicationContext, StockActivity::class.java)
                                intent.putExtra("name", name)
                                intent.putExtra("token", token)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            }

                        } else if (!obj.getBoolean("status")) {

                            val error = obj.getString("error")
                            Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(applicationContext, "Connection error..", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Add Sites", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error -> Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    override fun onBackPressed() {
        backButtonHandler()
        return
    }

    fun backButtonHandler() {
        val alertDialog = AlertDialog.Builder(this@Login)
        alertDialog.setTitle("Leave application?")
        alertDialog.setMessage("Are you sure you want to leave the application?")
        alertDialog.setIcon(R.mipmap.ic_launcher_round)
        alertDialog.setPositiveButton("YES"
        ) { dialog, which -> finish() }
        alertDialog.setNegativeButton("NO") { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    companion object {
        private val SHARED_PREF_NAME = "SriSaiKrishnapref"
        private val KEY_ID = "keyid"
        private val KEY_BRANCH_ID = "branch_id"
    }
}

