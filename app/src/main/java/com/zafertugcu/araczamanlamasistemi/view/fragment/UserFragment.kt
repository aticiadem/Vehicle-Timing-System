package com.zafertugcu.araczamanlamasistemi.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zafertugcu.araczamanlamasistemi.databinding.FragmentUserBinding
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.adapter.PastUsesAdapter
import com.zafertugcu.araczamanlamasistemi.adapter.VehicleAdapter
import com.zafertugcu.araczamanlamasistemi.databinding.ActivityMainBinding
import com.zafertugcu.araczamanlamasistemi.databinding.DialogAddVehicleBinding
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.PastUsesViewModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var mVehicleViewModel: VehicleViewModel
    private lateinit var mPastUsesViewModel: PastUsesViewModel
    private lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var pastUsesAdapter: PastUsesAdapter
    private var vehicleList = emptyList<VehicleInfoModel>()
    private lateinit var sharedPref: SharedPreferences
    private var runnable: Runnable = Runnable {  }
    var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences(getString(R.string.past_shared), Context.MODE_PRIVATE)

        mVehicleViewModel = ViewModelProvider(requireActivity()).get(VehicleViewModel::class.java)
        mPastUsesViewModel = ViewModelProvider(requireActivity()).get(PastUsesViewModel::class.java)

        vehicleAdapter = VehicleAdapter(requireContext(), sharedPref, mVehicleViewModel, mPastUsesViewModel)
        pastUsesAdapter = PastUsesAdapter(requireContext())
        binding.recyclerViewVehicleList.adapter = vehicleAdapter
        binding.recyclerViewPastUses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPastUses.adapter = pastUsesAdapter

        mVehicleViewModel.readAllData.observe(requireActivity(), { vehicle ->
            vehicleAdapter.setData(vehicle)
            vehicleList = vehicle
        })

        mPastUsesViewModel.readAllData.observe(requireActivity(), { pastUses ->
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

                val finished = "Bitiş Sayısı: $finishedCount"
                val reseted = "Reset Sayısı: $resetedCount"
                val total = "Toplam Kullanım: ${(finishedCount + resetedCount)}"

                binding.textViewNumberOfFinish.text = finished
                binding.textViewNumberOfReset.text = reseted
                binding.textViewTotalUsage.text = total

                handler.postDelayed(this,1000)
            }
        }
        handler.post(runnable)

    }

    private fun deletePast(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes){ _, _ ->
            mPastUsesViewModel.deletePastUses()
            val editor = sharedPref.edit()
            editor.putInt("finished",0)
            editor.putInt("reseted",0)
            editor.apply()
            Toast.makeText(requireContext(), R.string.past_deleted,Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.no){ _, _ -> }
        builder.setTitle(R.string.delete_past)
        builder.setMessage(R.string.are_you_sure_delete_past)
        builder.create().show()
    }

    /*private fun showAlertDialog(){
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogAddVehicleBinding
            .inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        dialogBinding.buttonSaveVehicle.setOnClickListener {
            val vehicleName = dialogBinding.editTextVehicleName.text
            val vehicleTime = dialogBinding.editTextVehicleTime.text
            if (vehicleName.isNotEmpty() && vehicleTime.isNotEmpty()){
                insertDataToDatabase(vehicleName.toString(), vehicleTime.toString().toInt())
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), R.string.fill_in_the_blanks,Toast.LENGTH_SHORT).show()
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
        Toast.makeText(requireContext(), R.string.save_is_successful,Toast.LENGTH_SHORT).show()
    }*/

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

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        _binding = null
    }

}