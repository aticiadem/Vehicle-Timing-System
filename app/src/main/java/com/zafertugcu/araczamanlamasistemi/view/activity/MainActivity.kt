package com.zafertugcu.araczamanlamasistemi.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zafertugcu.araczamanlamasistemi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*sharedPref = this.getSharedPreferences(getString(R.string.past_shared), Context.MODE_PRIVATE)

        mVehicleViewModel = ViewModelProvider(this).get(VehicleViewModel::class.java)
        mPastUsesViewModel = ViewModelProvider(this).get(PastUsesViewModel::class.java)

        vehicleAdapter = VehicleAdapter(this, sharedPref, mVehicleViewModel, mPastUsesViewModel)
        pastUsesAdapter = PastUsesAdapter(this)
        binding.recyclerViewVehicleList.adapter = vehicleAdapter
        binding.recyclerViewPastUses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPastUses.adapter = pastUsesAdapter

        mVehicleViewModel.readAllData.observe(this, { vehicle ->
            vehicleAdapter.setData(vehicle)
            vehicleList = vehicle
        })

        mPastUsesViewModel.readAllData.observe(this, { pastUses ->
            pastUsesAdapter.setData(pastUses)
        })

        binding.buttonClearPast.setOnClickListener {
            deletePast()
        }

        runnable = object : Runnable{
            override fun run() {
                updateDataTime(vehicleList)

                val finishedCount = sharedPref.getInt("finished",0)
                val resetedCount = sharedPref.getInt("reseted",0)

                val finished = getString(R.string.number_of_finish) + finishedCount.toString()
                val reseted = getString(R.string.number_of_reset) + resetedCount.toString()
                val total = getString(R.string.total_usage) + (finishedCount + resetedCount).toString()

                binding.textViewNumberOfFinish.text = finished
                binding.textViewNumberOfReset.text = reseted
                binding.textViewTotalUsage.text = total

                handler.postDelayed(this,1000)
            }
        }
        handler.post(runnable)*/

    }

}