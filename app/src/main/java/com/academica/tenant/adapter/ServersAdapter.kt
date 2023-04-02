package com.academica.tenant.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.academica.tenant.databinding.LayoutServerBinding
import com.academica.tenant.models.PenjualanModels
import com.academica.tenant.models.ServerModels

class ServersAdapter(private var context: Context,private var serverModels: List<ServerModels>) : RecyclerView.Adapter<ServersAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:LayoutServerBinding) : RecyclerView.ViewHolder(binding.root)

    private var onClickDelete: ((ServerModels) -> Unit)? = null
    private var onClickUpdate: ((ServerModels) -> Unit)? = null
    fun setOnClickDelete(callback:(ServerModels)-> Unit){
        this.onClickDelete = callback
    }
    fun setOnClickUpdate(callback: (ServerModels) -> Unit){
        this.onClickUpdate = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutServerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(serverModels[position]){
                binding.tvAlamatServer.text = this.address
                binding.tvNameServer.text = this.name
                binding.btnEdit.setOnClickListener {
                    onClickUpdate?.invoke(serverModels[position])
                }
                binding.btnDelete.setOnClickListener {
                    onClickDelete?.invoke(serverModels[position])
                }

            }
        }
    }

    override fun getItemCount(): Int {
       return serverModels.size
    }


}