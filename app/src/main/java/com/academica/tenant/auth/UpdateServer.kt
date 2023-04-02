package com.academica.tenant.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.academica.tenant.R
import com.academica.tenant.core.db.SQLiteHelper
import com.academica.tenant.databinding.ActivityUpdateServerBinding

class UpdateServer : AppCompatActivity() {
    lateinit var binding: ActivityUpdateServerBinding
    lateinit var sqLiteHelper: SQLiteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateServerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sqLiteHelper = SQLiteHelper(this)
        initData()
        binding.btnSaveServer.setOnClickListener {
            sqLiteHelper.updateServer(intent.getIntExtra("id", 0), binding.etAlamatServer.text.toString(), binding.etNameServer.text.toString())
            startActivity(Intent(this, ListServers::class.java))
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish()
        }
    }

    fun initData(){
        binding.etAlamatServer.setText(intent.getStringExtra("address"))
        binding.etNameServer.setText(intent.getStringExtra("name"))
    }



}