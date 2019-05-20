package com.SriSaiKrishnConstruction.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.SriSaiKrishnConstruction.R
import com.SriSaiKrishnConstruction.api.URLs
import com.SriSaiKrishnConstruction.model.VolleySingleton
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap

class AddTrip : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null
    internal lateinit var btn_time: Button
    private var mHour: Int = 0
    private var mMinute: Int = 0
    internal lateinit var time: EditText
    internal lateinit var owner: EditText
    internal lateinit var trip: EditText

    internal lateinit var calendar_issue: CalendarView
    internal var issue_datetext: TextView? = null

    private val SHARED_PREF_NAME = "SriSaiKrishnapref"

    internal var vehicle: EditText? = null
    internal var vehicle_name: Button? = null
    internal var submit_stock: Button? = null

     var times="";
     var owners="";
     var trips="";
     var issue_datetextss="";
     var vehicles="";

    var getIds="";
    var token="";

    val item_ids = ArrayList<String>();
    val item_names = ArrayList<String>();
    val item_ids2 = ArrayList<String>();
    val item_names2 = ArrayList<String>();

    internal lateinit var Item: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.add_trip, container, false)
        val sp = activity!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        token = sp.getString("keyid", "")
        VehicleList()
        time = view.findViewById<View>(R.id.time) as EditText
        trip = view.findViewById<View>(R.id.trip) as EditText
        owner = view.findViewById<View>(R.id.owner) as EditText
        btn_time = view.findViewById<View>(R.id.btn_time) as Button
        btn_time.setOnClickListener {
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(activity,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> time.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
            timePickerDialog.show()
        }
        calendar_issue = view?.findViewById<CalendarView>(R.id.calendar_issue2)!!
        issue_datetext= view?.findViewById<TextView>(R.id.issue_datetext)
        calendar_issue.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
            issue_datetext?.setText(date)
        }
        vehicle = view.findViewById<View>(R.id.vehicless) as EditText
        vehicle_name = view.findViewById<Button>(R.id.vehicle_name)
        vehicle_name?.setOnClickListener(View.OnClickListener { v ->  showProduct(v) })
        submit_stock = view?.findViewById<View>(R.id.add_trip) as Button
        submit_stock?.setOnClickListener(View.OnClickListener { v ->  attemptTrip() })

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

        fun newInstance(param1: String, param2: String): AddTrip {
            val fragment = AddTrip()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
    fun VehicleList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLs.URL_VECHICLES,
                Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getBoolean("status")) {

                            val userJson = obj.getJSONArray("vehicles")
                            for (i in 0 until userJson.length()) {

                                val itemslist = userJson.getJSONObject(i)
                                val item_id = itemslist.getString("id")
                                item_ids.add(item_id)
                                val item_type = itemslist.getString("vehicle_no")
                                item_names.add(item_type)
                                val item_type2 = itemslist.getString("owner")
                                item_names2.add(item_type2)
                            }
                        } else {
                            Toast.makeText(activity, "No vehicles added", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["token"] = token
                return params
            }
        }
        VolleySingleton.getInstance(activity!!).addToRequestQueue(stringRequest)
    }

    private fun showProduct(view: View) {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.vehicles_dialog)
        Item = dialog.findViewById(R.id.List)
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, item_names)
        Item.adapter = adapter
        Item.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            vehicle?.setText(item_names[position])
            owner?.setText(item_names2[position])
            getIds = item_ids[position]
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun attemptTrip() {

        issue_datetextss=issue_datetext?.getText().toString()
        times = time?.getText().toString()
        owners = owner?.getText().toString()
        trips = trip?.getText().toString()
        vehicles = vehicle?.getText().toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(issue_datetextss)) {
            issue_datetext?.setError(getString(R.string.error_field_required))
            focusView = issue_datetext
            cancel = true
        }
        if (TextUtils.isEmpty(times)) {
            time?.setError(getString(R.string.error_field_required))
            focusView = time
            cancel = true
        }
        if (TextUtils.isEmpty(vehicles)) {
            vehicle?.setError(getString(R.string.error_field_required))
            focusView = vehicle
            cancel = true
        }
        if (TextUtils.isEmpty(owners)) {
            owner?.setError(getString(R.string.error_field_required))
            focusView = owner
            cancel = true
        }
        if (TextUtils.isEmpty(trips)) {
            trip?.setError(getString(R.string.error_field_required))
            focusView = trip
            cancel = true
        }
        if (cancel) {

            focusView!!.requestFocus()

        } else {
            AddTrips()
        }
    }

    fun AddTrips() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLs.URL_TRIP,
                Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getBoolean("status")) {

                            Toast.makeText(activity, "Trips Added", Toast.LENGTH_SHORT).show()

                            issue_datetext?.setText("")
                            time?.setText("")
                            owner?.setText("")
                            trip?.setText("")
                            vehicle?.setText("")

                        } else {
                            Toast.makeText(activity, "No Trips Added", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["token"] = token
                params["date"] = issue_datetextss
                params["time"] = times
                params["vehicle_id"] = getIds
                params["owner"] = owners
                params["trip"] = trips

                return params
            }
        }
        VolleySingleton.getInstance(activity!!).addToRequestQueue(stringRequest)
    }
}
