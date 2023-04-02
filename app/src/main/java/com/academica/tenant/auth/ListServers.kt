package com.academica.tenant.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.academica.tenant.R
import com.academica.tenant.adapter.ServersAdapter
import com.academica.tenant.core.db.SQLiteHelper
import com.academica.tenant.databinding.ActivityListServersBinding

class ListServers : AppCompatActivity() {
    lateinit var binding: ActivityListServersBinding
    lateinit var sqLiteHelper: SQLiteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListServersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sqLiteHelper = SQLiteHelper(this)

        getAllServers()
    }

    fun getAllServers(){
        val serverList = sqLiteHelper.getAllServers()
        Log.d("TAG", "getAllServers: " + serverList.size)

        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.rvServer.layoutManager = layoutManager
        var serversAdapter: ServersAdapter = ServersAdapter(this,serverList)
        serversAdapter.setOnClickDelete {
            deletedServer(it.id)
            serversAdapter.notifyDataSetChanged()

        }

        serversAdapter.setOnClickUpdate {
            val intent = Intent(this, UpdateServer::class.java)
            intent.putExtra("id", it.id)
            intent.putExtra("address", it.address)
            intent.putExtra("name", it.name)
            startActivity(intent)
        }

        binding.rvServer.adapter = serversAdapter
        serversAdapter.notifyDataSetChanged()
    }

    fun deletedServer(id:Int){
        if (id == null) return
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah ingin menghapus server ?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            sqLiteHelper.deleteServer(id)
            dialog.dismiss()
        }

        builder.setNegativeButton("No"){dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
}