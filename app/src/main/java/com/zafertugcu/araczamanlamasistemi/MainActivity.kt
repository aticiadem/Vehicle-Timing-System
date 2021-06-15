package com.zafertugcu.araczamanlamasistemi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zafertugcu.araczamanlamasistemi.adapter.VehicleAdapter
import com.zafertugcu.araczamanlamasistemi.databinding.ActivityMainBinding
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var vehicleList: ArrayList<VehicleInfoModel>
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vehicleList = ArrayList()

        val x = VehicleInfoModel(1,360)
        val x1 = VehicleInfoModel(2,360)
        val x2 = VehicleInfoModel(3,360)
        val x3 = VehicleInfoModel(4,360)
        val x4 = VehicleInfoModel(5,360)
        val x5 = VehicleInfoModel(6,360)
        val x6 = VehicleInfoModel(7,360)
        val x7 = VehicleInfoModel(8,360)
        val x8 = VehicleInfoModel(9,360)
        val x9 = VehicleInfoModel(10,360)

        vehicleList.add(x)
        vehicleList.add(x1)
        vehicleList.add(x2)
        vehicleList.add(x3)
        vehicleList.add(x4)
        vehicleList.add(x5)
        vehicleList.add(x6)
        vehicleList.add(x7)
        vehicleList.add(x8)
        vehicleList.add(x9)

        vehicleAdapter = VehicleAdapter(vehicleList)

        //gridLayoutManager = GridLayoutManager(applicationContext,4, LinearLayoutManager.VERTICAL,false)
        //binding.recyclerViewVehicleList?.layoutManager = gridLayoutManager
        binding.recyclerViewVehicleList?.setHasFixedSize(true)

        binding.recyclerViewVehicleList?.adapter = vehicleAdapter
    }

}