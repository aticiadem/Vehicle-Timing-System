package com.zafertugcu.araczamanlamasistemi.view.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zafertugcu.araczamanlamasistemi.databinding.FragmentUserBinding
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.adapter.PastUsesAdapter
import com.zafertugcu.araczamanlamasistemi.adapter.VehicleAdapter
import com.zafertugcu.araczamanlamasistemi.databinding.DialogFinishedVehicleBinding
import com.zafertugcu.araczamanlamasistemi.model.PastUsesModel
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.PastUsesViewModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel
import java.text.SimpleDateFormat
import java.util.*

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
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
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

        runnable = object : Runnable {
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

    private fun updateDataTime(vehicleList: List<VehicleInfoModel>){
        for(list in vehicleList){
            if(list.vehicleIsStarted && list.vehicleTime > 0){
                val currentVehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime-1,
                    list.vehicleColor,
                    list.vehicleIsStarted,
                    2,
                    list.vehicleFinishText,
                    true
                )
                mVehicleViewModel.updateVehicle(currentVehicle)
            }
            if(list.vehicleIsStarted && list.vehicleTime == 0 && list.vehicleIsFinished == 2){
                val date = getCurrentDateTime()
                val dateInString = date.toString("dd/MM/yyyy  -  HH:mm:ss")
                val pastUse = PastUsesModel(
                    0,
                    dateInString,
                    list.vehicleName,
                    0
                )
                mPastUsesViewModel.addPastUse(pastUse)

                dialogFinishedVehicle(list.vehicleName)

                val oldValue = sharedPref.getInt("finished",0)
                val newValue = oldValue + 1
                val editor = sharedPref.edit()
                editor.putInt("finished",newValue)
                editor.apply()

                val vehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleFinishText,
                    list.vehicleMainTime,
                    list.vehicleTime,
                    list.vehicleColor,
                    list.vehicleIsStarted,
                    1,
                    list.vehicleName,
                    true
                )
                mVehicleViewModel.updateVehicle(vehicle)
            }
            if(list.vehicleIsStarted && list.vehicleTime == 0 && list.vehicleIsFinished == 1){
                val vehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime,
                    list.vehicleColor,
                    list.vehicleIsStarted,
                    0,
                    list.vehicleFinishText,
                    true
                )
                mVehicleViewModel.updateVehicle(vehicle)
            }
            if(list.vehicleIsStarted && list.vehicleTime == 0 && list.vehicleIsFinished == 0){
                val vehicle = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleTime,
                    list.vehicleColor,
                    list.vehicleIsStarted,
                    1,
                    list.vehicleFinishText,
                    true
                )
                mVehicleViewModel.updateVehicle(vehicle)
            }
            /*if((list.vehicleMainTime != list.vehicleTime) &&
                list.vehicleTime == 0){
                val date = getCurrentDateTime()
                val dateInString = date.toString("dd/MM/yyyy  -  HH:mm:ss")

                if (!list.vehicleAdded){
                    val pastUse = PastUsesModel(
                        0,
                        dateInString,
                        list.vehicleName,
                        0
                    )
                    mPastUsesViewModel.addPastUse(pastUse)

                    val newData = VehicleInfoModel(
                        list.vehicleId,
                        list.vehicleName,
                        list.vehicleMainTime,
                        0,
                        list.vehicleColor,
                        false,
                        1,
                        true
                    )
                    mVehicleViewModel.updateVehicle(newData)

                    val oldValue = sharedPref.getInt("finished",0)
                    val newValue = oldValue + 1
                    val editor = sharedPref.edit()
                    editor.putInt("finished",newValue)
                    editor.apply()
                }
                val newData = VehicleInfoModel(
                    list.vehicleId,
                    list.vehicleName,
                    list.vehicleMainTime,
                    list.vehicleMainTime,
                    list.vehicleColor,
                    false,
                    2,
                    false
                )
                mVehicleViewModel.updateVehicle(newData)
            }*/
        }
    }

    private fun dialogFinishedVehicle(name: String){
        val dialog = Dialog(requireContext())
        val dialogFinishedVehicleBinding = DialogFinishedVehicleBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogFinishedVehicleBinding.root)

        dialogFinishedVehicleBinding.textViewFinishedVehicle.text = name

        dialogFinishedVehicleBinding.buttonOK.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format,locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        _binding = null
    }

}