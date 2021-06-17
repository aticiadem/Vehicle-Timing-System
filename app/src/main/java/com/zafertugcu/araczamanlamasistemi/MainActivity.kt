package com.zafertugcu.araczamanlamasistemi

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zafertugcu.araczamanlamasistemi.adapter.VehicleAdapter
import com.zafertugcu.araczamanlamasistemi.databinding.ActivityMainBinding
import com.zafertugcu.araczamanlamasistemi.databinding.DialogAddVehicleBinding
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mVehicleViewModel: VehicleViewModel
    private lateinit var adapter: VehicleAdapter
    private var vehicleList = emptyList<VehicleInfoModel>()
    var runnable: Runnable = Runnable {  }
    var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mVehicleViewModel = ViewModelProvider(this).get(VehicleViewModel::class.java)

        adapter = VehicleAdapter(this, mVehicleViewModel)
        binding.recyclerViewVehicleList.adapter = adapter

        mVehicleViewModel.readAllData.observe(this,  { vehicle ->
            adapter.setData(vehicle)
            vehicleList = vehicle
        })

        runnable = object : Runnable{
            override fun run() {
                updateDataTime(vehicleList)
                handler.postDelayed(this,1000)
            }
        }
        handler.post(runnable)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.vehicle_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionAddVehicle -> {
                showAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(){
        val dialog = Dialog(this)
        val dialogBinding = DialogAddVehicleBinding
            .inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialogBinding.buttonSaveVehicle.setOnClickListener {
            val vehicleName = dialogBinding.editTextVehicleName.text
            val vehicleTime = dialogBinding.editTextVehicleTime.text
            if (vehicleName.isNotEmpty() && vehicleTime.isNotEmpty()){
                insertDataToDatabase(vehicleName.toString(), vehicleTime.toString().toInt())
                dialog.dismiss()
            } else {
                Toast.makeText(this,R.string.fill_in_the_blanks,Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun insertDataToDatabase(vehicleName: String, vehicleTime: Int){
        val vehicle = VehicleInfoModel(
            0,
            vehicleName,
            vehicleTime,
            vehicleTime
        )
        mVehicleViewModel.addVehicle(vehicle)
        Toast.makeText(this,R.string.save_is_successful,Toast.LENGTH_SHORT).show()
    }

    private fun updateDataTime(vehicleList: List<VehicleInfoModel>){
        for(list in vehicleList){
            if(list.vehicleIsStarted && list.vehicleTime > 0){
                val curretVehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime-1,
                    list.vehicleIsStarted
                )
                mVehicleViewModel.updateVehicle(curretVehicle)
            }
        }
    }

}