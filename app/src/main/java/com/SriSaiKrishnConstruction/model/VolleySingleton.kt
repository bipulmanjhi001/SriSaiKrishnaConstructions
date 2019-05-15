package com.SriSaiKrishnConstruction.model

import android.content.Context

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context) {
    private var mRequestQueue: RequestQueue? = null

    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.applicationContext)
            }
            return mRequestQueue
        }

    init {
        mCtx = context
        mRequestQueue = requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue?.add(req)
    }

    companion object {
        private var mInstance: VolleySingleton? = null
        private lateinit var mCtx: Context

        @Synchronized
        fun getInstance(context: Context): VolleySingleton {
            if (mInstance == null) {
                mInstance = VolleySingleton(context)
            }
            return mInstance as VolleySingleton
        }
    }
}