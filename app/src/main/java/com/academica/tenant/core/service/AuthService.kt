package com.academica.tenant.core.service

import android.app.Activity
import com.academica.tenant.core.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthService {

    @GET("account/merchant_login/{user}/{pass}")
    fun login(@Path("user") user: String, @Path("pass") pass: String) : Call<ResponseBody>

    @GET("account/getmerchantid/{id}/m")
    fun getProfile(@Path("id") id: String) : Call<ResponseBody>

    companion object {
        fun create(activity: Activity) : AuthService {
            return RetrofitClient.getClient(activity).create(AuthService::class.java)
        }
    }
}