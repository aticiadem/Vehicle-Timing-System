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

        //supportActionBar?.hide()
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

    /*private fun deletePast(){
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(R.string.yes){ _, _ ->
            mPastUsesViewModel.deletePastUses()
            val editor = sharedPref.edit()
            editor.putInt("finished",0)
            editor.putInt("reseted",0)
            editor.apply()
            Toast.makeText(this, R.string.past_deleted,Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.no){ _, _ -> }
        builder.setTitle(R.string.delete_past)
        builder.setMessage(R.string.are_you_sure_delete_past)
        builder.create().show()
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
                Toast.makeText(this, R.string.fill_in_the_blanks,Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, R.string.save_is_successful,Toast.LENGTH_SHORT).show()
    }

    private fun updateDataTime(vehicleList: List<VehicleInfoModel>){
        for(list in vehicleList){
            if(list.vehicleIsStarted && list.vehicleTime > 0){
                val currentVehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime-1,
                    list.vehicleIsStarted,
                    2
                )
                mVehicleViewModel.updateVehicle(currentVehicle)
            }
            if(list.vehicleIsStarted && list.vehicleTime == 0 && list.vehicleIsFinished == 2){
                val vehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime,
                    list.vehicleIsStarted,
                    1
                )
                mVehicleViewModel.updateVehicle(vehicle)
            }
            if(list.vehicleIsStarted && list.vehicleTime == 0 && list.vehicleIsFinished == 1){
                val vehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime,
                    list.vehicleIsStarted,
                    0
                )
                mVehicleViewModel.updateVehicle(vehicle)
            }
            if(list.vehicleIsStarted && list.vehicleTime == 0 && list.vehicleIsFinished == 0){
                val vehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime,
                    list.vehicleIsStarted,
                    1
                )
                mVehicleViewModel.updateVehicle(vehicle)
            }
        }
    }
     */

}