package com.academica.tenant.core.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Connection(context: Context) {
    private val context = context

    fun isConnectionInternet(): Boolean{
        val connectivy = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivy != null){
            val info = connectivy.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }

        return false
    }
}