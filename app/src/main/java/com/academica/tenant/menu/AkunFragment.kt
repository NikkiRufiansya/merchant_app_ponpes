package com.academica.tenant.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.academica.tenant.core.helper.Connection
import com.academica.tenant.core.helper.Currency
import com.academica.tenant.core.helper.SessionManager
import com.academica.tenant.core.service.AuthService
import com.academica.tenant.databinding.FragmentAkunBinding
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AkunFragment : Fragment() {
    lateinit var binding: FragmentAkunBinding
    lateinit var connection: Connection
    lateinit var authService: AuthService
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAkunBinding.inflate(layoutInflater, container, false)
        connection = Connection(requireContext())
        sessionManager = SessionManager(requireContext())
        authService = AuthService.create(requireActivity())
        getProfile()
        binding.cardLogout.setOnClickListener {
            sessionManager.logoutUser()
            requireActivity().finish()
        }
        return binding.root
    }

    fun getProfile(){
        authService.getProfile(sessionManager.getId()).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var responseBody = response.body()?.string()
                var jsonObject = JSONObject(responseBody)
                if (jsonObject.getBoolean("success")){
                    binding.tvName.text = "Nama : " + jsonObject.getString("name")
                    binding.tvTotalSaldo.text = "Total Saldo : Rp " + Currency.nominal(jsonObject.getString("saldo"))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    companion object {
        fun getInstance(): AkunFragment = AkunFragment()

    }
}