package com.academica.tenant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.academica.tenant.core.helper.Currency
import com.academica.tenant.databinding.TablePenjualanBinding
import com.academica.tenant.models.PenjualanModels

class PenjualanAdapter(private var penjualanModels: List<PenjualanModels>) : RecyclerView.Adapter<PenjualanAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: TablePenjualanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TablePenjualanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(penjualanModels[position]){
                binding.tvNo.text = this.no
                binding.tvNama.text = this.nama
                binding.tvJumlah.text = "Rp "+Currency.nominal(this.Jumlah)
                binding.tvTanggal.text = this.date
            }
        }
    }

    override fun getItemCount(): Int {
        return penjualanModels.size
    }
}
