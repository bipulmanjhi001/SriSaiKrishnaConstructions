package com.SriSaiKrishnConstruction.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.SriSaiKrishnConstruction.R
import com.SriSaiKrishnConstruction.model.TabAdapter

class Maintain_Stock : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mListener: OnFragmentInteractionListener? = null

    internal lateinit var adapters: TabAdapter
    internal lateinit var tabLayouts: TabLayout
    internal lateinit var viewPagers: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.stock_form, container, false)
        viewPagers = view.findViewById<View>(R.id.stockist) as ViewPager
        tabLayouts = view.findViewById<View>(R.id.stocktab) as TabLayout
        adapters = TabAdapter(activity!!.supportFragmentManager)

        adapters.addFragment(StockIn(), "Stock In")
        adapters.addFragment(StockOut(), "Stock Out")
        viewPagers.adapter = adapters
        tabLayouts.setupWithViewPager(viewPagers)
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

        fun newInstance(param1: String, param2: String): Maintain_Stock {
            val fragment = Maintain_Stock()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
