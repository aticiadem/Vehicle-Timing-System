package com.zafertugcu.araczamanlamasistemi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zafertugcu.araczamanlamasistemi.databinding.VehicleRecyclerRowBinding
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel

class VehicleAdapter(private val vehicleList: ArrayList<VehicleInfoModel>)
    : RecyclerView.Adapter<VehicleAdapter.VehicleViewModel>() {

    inner class VehicleViewModel(val itemBinding: VehicleRecyclerRowBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewModel {
        val binding = VehicleRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VehicleViewModel(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewModel, position: Int) {
        holder.itemBinding.textViewNumber.text = vehicleList[position].vehicleNumber.toString()
        holder.itemBinding.textViewTime.text = vehicleList[position].vehicleTime.toString()
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

}