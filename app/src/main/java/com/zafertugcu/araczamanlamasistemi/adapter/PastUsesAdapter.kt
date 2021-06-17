package com.zafertugcu.araczamanlamasistemi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.databinding.PastUsesRecyclerRowBinding
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel

class PastUsesAdapter(private val context: Context)
    : RecyclerView.Adapter<PastUsesAdapter.PastUsesViewHolder>() {

    private var pastUsesList = emptyList<PastUsesModel>()

    inner class PastUsesViewHolder(val itemBinding: PastUsesRecyclerRowBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastUsesViewHolder {
        val binding = PastUsesRecyclerRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return PastUsesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PastUsesViewHolder, position: Int) {
        val currentItem = pastUsesList[position]
        holder.itemBinding.textViewDate.text = currentItem.pastDate
        holder.itemBinding.textViewVehicleName.text = currentItem.pastVehicleName
        if(currentItem.pastState == 0){
            holder.itemBinding.textViewState.text = context.resources.getString(R.string.finished)
        } else {
            holder.itemBinding.textViewState.text = context.resources.getString(R.string.reseted)
        }
    }

    override fun getItemCount(): Int {
        return pastUsesList.size
    }

    fun setData(pastUsesList: List<PastUsesModel>){
        this.pastUsesList = pastUsesList
        notifyDataSetChanged()
    }

}