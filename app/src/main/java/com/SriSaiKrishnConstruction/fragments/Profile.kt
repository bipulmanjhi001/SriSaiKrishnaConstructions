package com.SriSaiKrishnConstruction.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.SriSaiKrishnConstruction.R
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.widget.TextView
import com.SriSaiKrishnConstruction.main.Login
import com.SriSaiKrishnConstruction.pref.SharedPrefManager

class Profile : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    private val SHARED_PREF_NAME = "Profilesinghpref"
    private val KEY_NAME = "s_name"
    private val KEY_ADDRESS = "address"
    private val KEY_DESC = "description"
    private val KEY_SUPERVISOR = "supervisor_name"

    var name: String? = null
    var address: String? = null
    var desc: String? = null
    var supervisor: String? = null

    internal var p_name: TextView? = null
    internal var p_email: TextView? = null
    internal var p_phone: TextView? = null
    internal var p_sign: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile, container, false)

        val sp = activity!!.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        name = sp.getString(KEY_NAME, "")
        address = sp.getString(KEY_ADDRESS, "")
        desc = sp.getString(KEY_DESC, "")
        supervisor = sp.getString(KEY_SUPERVISOR, "")
        p_name= view?.findViewById<TextView>(R.id.p_name)
        p_name?.setText(name)
        p_email= view?.findViewById<TextView>(R.id.p_email)
        p_email?.setText(address)
        p_phone= view?.findViewById<TextView>(R.id.p_phone)
        p_phone?.setText(desc)
        p_sign= view?.findViewById<TextView>(R.id.p_sign)

        p_sign?.setOnClickListener(View.OnClickListener { v ->
            SharedPrefManager.getInstance(activity!!).logout()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
            activity!!.finish() })

        return view
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        fun newInstance(param1: String, param2: String): Profile {
            val fragment = Profile()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
