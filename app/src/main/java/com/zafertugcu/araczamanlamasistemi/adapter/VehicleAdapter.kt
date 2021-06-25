package com.zafertugcu.araczamanlamasistemi.adapter

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.databinding.DialogVehicleClickBinding
import com.zafertugcu.araczamanlamasistemi.databinding.DialogVehicleDetailBinding
import com.zafertugcu.araczamanlamasistemi.databinding.VehicleRecyclerRowBinding
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.PastUsesViewModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel
import java.text.SimpleDateFormat
import java.util.*

class VehicleAdapter(
    private val context: Context,
    private val sharedPref: SharedPreferences,
    private val mVehicleViewModel: com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel,
    private val mPastUsesViewModel: PastUsesViewModel
): RecyclerView.Adapter<VehicleAdapter.VehicleViewModel>() {

    private var vehicleList = emptyList<VehicleInfoModel>()

    inner class VehicleViewModel(val itemBinding: VehicleRecyclerRowBinding)
        :RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewModel {
        val binding = VehicleRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VehicleViewModel(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewModel, position: Int) {
        val currentItem = vehicleList[position]
        holder.itemBinding.textViewNumber.text = currentItem.vehicleName
        holder.itemBinding.textViewTime.text = currentItem.vehicleTime.toString()

        when (currentItem.vehicleTime) {
            in 0..50 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red))
            }
            in 51..100 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red_yellow))
            }
            in 101..150 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red_yellow2))
            }
            in 151..200 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red_yellow3))
            }
            in 201..250 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.yellow))
            }
            in 251..300 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.yellow_green))
            }
            in 301..350 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.yellow_green2))
            }
            in 351..400 -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.yellow_green3))
            }
            else -> {
                holder.itemBinding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.green))
            }
        }

        changeColor(currentItem.vehicleIsFinished, holder.itemBinding.cardView)

        holder.itemBinding.cardView.setOnClickListener{
            showAlertDialog(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    fun setData(vehicleList: List<VehicleInfoModel>){
        this.vehicleList = vehicleList
        notifyDataSetChanged()
    }

    private fun showAlertDialog(currentData: VehicleInfoModel){
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy  -  HH:mm:ss")

        val dialog = Dialog(context)
        val dialogBinding = DialogVehicleClickBinding
            .inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.buttonStart.setOnClickListener {
            if(currentData.vehicleTime == currentData.vehicleMainTime){
                val newData = VehicleInfoModel(
                    currentData.vehicleId,
                    currentData.vehicleName,
                    currentData.vehicleMainTime,
                    currentData.vehicleTime,
                    true,
                    currentData.vehicleIsFinished
                )
                mVehicleViewModel.updateVehicle(newData)
                dialog.dismiss()
            } else {
                Toast.makeText(context,R.string.please_start_vehicle,Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.buttonFinish.setOnClickListener {
            if((currentData.vehicleMainTime != currentData.vehicleTime) &&
                    currentData.vehicleTime == 0){
                val pastUse = PastUsesModel(
                    0,
                    dateInString,
                    currentData.vehicleName,
                    0
                )
                mPastUsesViewModel.addPastUse(pastUse)
                val newData = VehicleInfoModel(
                    currentData.vehicleId,
                    currentData.vehicleName,
                    currentData.vehicleMainTime,
                    currentData.vehicleMainTime,
                    false,
                    2
                )
                mVehicleViewModel.updateVehicle(newData)

                val oldValue = sharedPref.getInt("finished",0)
                val newValue = oldValue + 1
                val editor = sharedPref.edit()
                editor.putInt("finished",newValue)
                editor.apply()

                dialog.dismiss()
            } else {
                Toast.makeText(context,R.string.data_is_not_finished,Toast.LENGTH_SHORT).show()
            }
        }

        dialogBinding.buttonReset.setOnClickListener {
            if(currentData.vehicleMainTime != currentData.vehicleTime){
                val pastUse = PastUsesModel(
                    0,
                    dateInString,
                    currentData.vehicleName,
                    1
                )
                mPastUsesViewModel.addPastUse(pastUse)
                val newData = VehicleInfoModel(
                    currentData.vehicleId,
                    currentData.vehicleName,
                    currentData.vehicleMainTime,
                    currentData.vehicleMainTime,
                    false,
                    2
                )
                mVehicleViewModel.updateVehicle(newData)

                val oldValue = sharedPref.getInt("reseted",0)
                val newValue = oldValue + 1
                val editor = sharedPref.edit()
                editor.putInt("reseted",newValue)
                editor.apply()

                dialog.dismiss()
                dialog.dismiss()
            } else {
                Toast.makeText(context,R.string.data_is_not_started,Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date{
        return Calendar.getInstance().time
    }

    private fun changeColor(vehicleIsFinished: Int, currentCardView: CardView){
        if(vehicleIsFinished == 1){
            currentCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.black))
        } else if(vehicleIsFinished == 0){
            currentCardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.red))
        }
    }

}