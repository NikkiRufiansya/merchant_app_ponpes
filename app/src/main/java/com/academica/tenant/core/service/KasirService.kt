package com.academica.tenant.core.service

import android.app.Activity
import com.academica.tenant.core.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface KasirService {

    @GET("/kasir/proses_s/{siswa_id}/{amount}/{merchant_id}/{pin}")
    fun pembayaran(
        @Path("siswa_id") siswa_id: String,
        @Path("amount") amount: String,
        @Path("merchant_id") merchant_id: String,
        @Path("pin") pin: String
    ): Call<ResponseBody>

    companion object {
        fun create(activity: Activity): KasirService {
            return RetrofitClient.getClient(activity).create(KasirService::class.java)
        }
    }
}