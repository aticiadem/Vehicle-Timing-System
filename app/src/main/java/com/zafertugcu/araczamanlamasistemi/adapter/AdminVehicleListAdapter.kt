package com.zafertugcu.araczamanlamasistemi.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.databinding.AdminVehiclesRecyclerRowBinding
import com.zafertugcu.araczamanlamasistemi.databinding.DialogVehicleDetailBinding
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel

class AdminVehicleListAdapter(
    private val context: Context,
    private val mVehicleViewModel: VehicleViewModel
    ): RecyclerView.Adapter<AdminVehicleListAdapter.VehicleListViewHolder>() {

    private var vehicleList = emptyList<VehicleInfoModel>()

    inner class VehicleListViewHolder(val itemBinding: AdminVehiclesRecyclerRowBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleListViewHolder {
        val binding = AdminVehiclesRecyclerRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return VehicleListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleListViewHolder, position: Int) {
        val currentItem = vehicleList[position]
        holder.itemBinding.textViewVehicleNumber.text = (position+1).toString()
        holder.itemBinding.textViewVehicleName.text = currentItem.vehicleName
        holder.itemBinding.textViewVehicleTime.text = currentItem.vehicleMainTime.toString()

        holder.itemBinding.imageViewDelete.setOnClickListener {
            deleteVehicle(currentItem)
        }

        holder.itemBinding.imageViewEdit.setOnClickListener {
            dialogEditVehicle(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    fun setData(vehicleList: List<VehicleInfoModel>){
        this.vehicleList = vehicleList
        notifyDataSetChanged()
    }

    private fun dialogEditVehicle(currentData: VehicleInfoModel) {
        val dialog = Dialog(context)
        val dialogBinding = DialogVehicleDetailBinding
            .inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.editTextDetailVehicleName.setText(currentData.vehicleName)
        dialogBinding.editTextDetailVehicleStartTime.setText(currentData.vehicleMainTime.toString())

        dialogBinding.buttonSaveChanges.setOnClickListener {
            val vehicleName = dialogBinding.editTextDetailVehicleName.text.toString()
            val startTime = dialogBinding.editTextDetailVehicleStartTime.text.toString()

            if ((vehicleName == currentData.vehicleName) &&
                startTime == currentData.vehicleMainTime.toString()
            ) {
                Toast.makeText(context, R.string.save_changes_error, Toast.LENGTH_SHORT).show()
            } else {
                val newData = VehicleInfoModel(
                    currentData.vehicleId,
                    vehicleName,
                    startTime.toInt(),
                    startTime.toInt(),
                    currentData.vehicleIsStarted,
                    currentData.vehicleIsFinished
                )
                mVehicleViewModel.updateVehicle(newData)
                Toast.makeText(context, R.string.changes_is_successful, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun deleteVehicle(currentData: VehicleInfoModel){
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton(R.string.yes){ _,_ ->
            mVehicleViewModel.deleteVehicle(currentData)
            Toast.makeText(context,R.string.vehicle_is_deleted,Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.no){ _,_ -> }
        builder.setMessage(R.string.are_you_sure_delete_vehicle)
        builder.setTitle(R.string.delete_vehicle)
        builder.create().show()
    }

}