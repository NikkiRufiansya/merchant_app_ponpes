package com.academica.tenant.core.service

import android.app.Activity
import com.academica.tenant.core.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HistoryService {

    @GET("kasir/history_m/{id}")
    fun getHistoryPenjualan(@Path("id") id: String): Call<ResponseBody>


    companion object {
        fun create(activity: Activity) : HistoryService {
            return RetrofitClient.getClient(activity).create(HistoryService::class.java)
        }
    }
}