package com.SriSaiKrishnConstruction.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
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
import java.util.HashMap

@SuppressLint("ValidFragment")
internal class StockOut : Fragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null

    private val SHARED_PREF_NAME = "SriSaiKrishnapref"

    var token="";
    var getIds="";
    var getIds2="";
    var getQty="";
    var issue_datetexts="";
    var qtys=";"

    internal lateinit var calendar_issue: CalendarView
    internal lateinit var Item: ListView
    internal lateinit var Item2: ListView

    internal var products: EditText? = null
    internal var qty: EditText? = null
    internal var suppliers: EditText? = null
    internal var remarks: EditText? = null
    internal var issue_datetext: TextView? = null
    internal var check_qty: TextView? = null

    internal var products_name: Button? = null
    internal var supplier_name: Button? = null
    internal var submit_stock: Button? = null

    val item_ids = ArrayList<String>();
    val item_names = ArrayList<String>();
    val item_ids2 = ArrayList<String>();
    val item_names2 = ArrayList<String>();
    val item_names3 = ArrayList<String>();

    var productsss: String? = null
    var supplierss: String? = null
    var remarkss: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.stock_out, container, false)

        val sp = activity!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        token = sp.getString("keyid", "")

        calendar_issue =  view?.findViewById<CalendarView>(R.id.calendar_issue)
        products = view?.findViewById<EditText>(R.id.products)

        qty = view?.findViewById<EditText>(R.id.qty)
        suppliers = view?.findViewById<EditText>(R.id.site)
        remarks = view?.findViewById<EditText>(R.id.remarks)
        issue_datetext= view?.findViewById<TextView>(R.id.issue_datetext)
        products_name = view?.findViewById<Button>(R.id.products_name)
        check_qty= view?.findViewById<TextView>(R.id.check_qty)

        ProductList();

        products_name?.setOnClickListener(View.OnClickListener { v -> showProduct(v) })
        supplier_name= view?.findViewById<Button>(R.id.site_name)
        supplier_name?.setOnClickListener(View.OnClickListener { v -> showSupplier(v) })
        calendar_issue.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = year.toString() + "-" + (month + 1) + "-" + dayOfMonth
            issue_datetext?.setText(date)
        }

        submit_stock = view?.findViewById<Button>(R.id.submit_stock)
        submit_stock?.setOnClickListener(View.OnClickListener { v ->
            qtys = qty?.getText().toString()
            val enterqty: Int = qtys.toInt()
            val check: Int = getQty.toInt()
            if(enterqty <= check)
            {
            attemptStock()
        }else {
                var cancel = false
                var focusView: View? = null
                qty?.setError(getString(R.string.error_field_required))
                focusView = qty
                cancel = true

                if (cancel) {
                    focusView!!.requestFocus()

                }
        } })
        return view;
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

        fun newInstance(param1: String, param2: String): StockOut {
            val fragment = StockOut()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private fun attemptStock() {

        productsss = products?.getText().toString()
        supplierss = suppliers?.getText().toString()
        remarkss = remarks?.getText().toString()
        issue_datetexts=issue_datetext?.getText().toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(productsss)) {
            products?.setError(getString(R.string.error_field_required))
            focusView = products
            cancel = true
        }

        if (TextUtils.isEmpty(supplierss)) {
            suppliers?.setError(getString(R.string.error_field_required))
            focusView = suppliers
            cancel = true
        }
        if (cancel) {

            focusView!!.requestFocus()

        } else {
            OutStock()
        }
    }

    fun ProductList() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLs.URL_STOCKPRODUCT,
                Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getBoolean("status")) {

                            val userJson = obj.getJSONArray("products")
                            for (i in 0 until userJson.length()) {

                                val itemslist = userJson.getJSONObject(i)
                                val item_id = itemslist.getString("product_id")
                                item_ids.add(item_id)
                                val item_type = itemslist.getString("product_name")
                                item_names.add(item_type)
                                val item_type2 = itemslist.getString("remaining")
                                item_names3.add(item_type2)
                            }
                        } else {
                            Toast.makeText(activity, "No products added", Toast.LENGTH_SHORT).show()
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
        AddSupplier();
    }

    private fun showProduct(view: View) {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.list_dialog)
        Item = dialog.findViewById(R.id.List)
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, item_names)
        Item.adapter = adapter
        Item.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            products?.setText(item_names[position])
            getIds = item_ids[position]
            getQty = item_names3[position]
            check_qty?.setText("Remaining : "+item_names3[position])
            dialog.dismiss()
        }
        dialog.show()
    }

    fun AddSupplier() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLs.URL_SITEUSERS,
                Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getBoolean("status")) {

                            val userJson = obj.getJSONArray("siteusers")
                            for (i in 0 until userJson.length()) {

                                val itemslist = userJson.getJSONObject(i)
                                val item_id = itemslist.getString("id")
                                item_ids2.add(item_id)
                                val item_type = itemslist.getString("name")
                                item_names2.add(item_type)

                            }
                        } else {

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

    private fun showSupplier(view: View) {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.supplier_dialog)
        Item2 = dialog.findViewById(R.id.List)
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, item_names2)
        Item2.adapter = adapter
        Item2.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            suppliers?.setText(item_names2[position])
            getIds2 = item_ids2[position]
            dialog.dismiss()
        }
        dialog.show()
    }

    fun OutStock() {
        val stringRequest = object : StringRequest(Request.Method.POST, URLs.URL_STOCKOUT,
                Response.Listener { response ->
                    try {
                        val obj = JSONObject(response)
                        if (obj.getBoolean("status")) {

                            Toast.makeText(activity, "Stock Out", Toast.LENGTH_SHORT).show()

                            products?.setText("")
                            qty?.setText("")
                            suppliers?.setText("")
                            remarks?.setText("")
                            issue_datetext?.setText("")
                            check_qty?.setText("Qty.")

                        } else {
                            Toast.makeText(activity, "No Stock Out", Toast.LENGTH_SHORT).show()
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
                params["date"] = issue_datetexts
                params["product_id"] = getIds
                params["quantity"] = qty?.getText().toString()
                params["site_user"] = getIds2
                params["remarks"] = remarks?.getText().toString()

                return params
            }
        }
        VolleySingleton.getInstance(activity!!).addToRequestQueue(stringRequest)
    }

}