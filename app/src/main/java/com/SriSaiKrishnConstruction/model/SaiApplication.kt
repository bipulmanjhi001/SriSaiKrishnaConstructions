package com.SriSaiKrishnConstruction.model

import android.app.Application

class SaiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    companion object {

        @get:Synchronized
        var instance: SaiApplication? = null
            private set
    }
}