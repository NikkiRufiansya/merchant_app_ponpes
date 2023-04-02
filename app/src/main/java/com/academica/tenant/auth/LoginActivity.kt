package com.academica.tenant.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.academica.tenant.MainActivity
import com.academica.tenant.R
import com.academica.tenant.core.db.SQLiteHelper
import com.academica.tenant.core.helper.Connection
import com.academica.tenant.core.helper.SessionManager
import com.academica.tenant.core.service.AuthService
import com.academica.tenant.databinding.ActivityLoginBinding
import com.academica.tenant.databinding.DialogAddServerBinding
import com.academica.tenant.models.ServerModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityLoginBinding
    lateinit var connection: Connection
    lateinit var sessionManager: SessionManager
    lateinit var authService: AuthService
    lateinit var sqLiteHelper: SQLiteHelper
    lateinit var dialog : BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connection = Connection(this)
        sessionManager = SessionManager(this)
        authService = AuthService.create(this)
        sqLiteHelper = SQLiteHelper(this)
        setOnClickListener()
    }

    fun dialogAddServer() {
        binding.btnAddServer.setOnClickListener {
           dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
            var dialogBinding = DialogAddServerBinding.inflate(LayoutInflater.from(this))
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.setContentView(dialogBinding.root)
            dialog.behavior.isDraggable = true
            dialogBinding.btnSaveServer.setOnClickListener {
                if (dialogBinding.etAlamatServer.text.isEmpty() || dialogBinding.etNameServer.text.isEmpty()){
                    Toast.makeText(this, "Alamat server & Name Server tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }else{
                    val servers = ServerModels(address = dialogBinding.etAlamatServer.text.toString(), name = dialogBinding.etNameServer.text.toString())
                    val status = sqLiteHelper.insertServer(servers)
                    if(status > -1){
                        Toast.makeText(this, "Server Tersimpan", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }else{

                    }

                }
            }

            dialogBinding.tvLihatServer.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ListServers::class.java))
            }


            dialog.show()
        }
    }

    private fun setOnClickListener() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnAddServer.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        var itemId = v.id
        when (itemId) {
            R.id.btn_login -> login()
            R.id.btn_add_server -> dialogAddServer()
        }
    }

    fun login() {
        try {
            if(!binding.etUsername.text.isEmpty() || !binding.etPassword.text.isEmpty()){
                if (connection.isConnectionInternet()) {
                    authService.login(
                        binding.etUsername.text.toString(),
                        binding.etPassword.text.toString()
                    ).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {

                            var response = response.body()?.string()
                            var jsonObject = JSONObject(response)
                            Log.d("LOGIN", "onResponse: --->"+response)
                            if (jsonObject.getBoolean("success")) {
                                sessionManager.setIsLogin()
                                sessionManager.setId(jsonObject.getString("id"))
                                sessionManager.setAccount(jsonObject.getString("account"))
                                sessionManager.setInit(jsonObject.getString("init"))
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()
                            }else{
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Username atau Password Salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("LOGIN", "onFailure: -->"+ t.message)
                        }
                    })

                }
            }else{
                Toast.makeText(this@LoginActivity, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.d("LOGIN", "Exception: -->"+ e.message)
        }
    }

    fun addServer(address: String, name: String){

    }
}