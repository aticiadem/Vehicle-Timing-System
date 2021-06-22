package com.zafertugcu.araczamanlamasistemi.view.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.adapter.AdminVehicleListAdapter
import com.zafertugcu.araczamanlamasistemi.databinding.FragmentAdminBinding
import com.zafertugcu.araczamanlamasistemi.view.activity.MainActivity
import com.zafertugcu.araczamanlamasistemi.viewmodel.PastUsesViewModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var vehicleListAdapter: AdminVehicleListAdapter
    //private var vehicleList = emptyList<VehicleInfoModel>()
    private lateinit var mVehicleViewModel: VehicleViewModel
    private lateinit var mPastUsesViewModel: PastUsesViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater,container,false)
        val view = binding.root
        (activity as MainActivity?)?.setSupportActionBar(binding.toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences(getString(R.string.past_shared), Context.MODE_PRIVATE)

        mVehicleViewModel = ViewModelProvider(requireActivity()).get(VehicleViewModel::class.java)
        mPastUsesViewModel = ViewModelProvider(requireActivity()).get(PastUsesViewModel::class.java)

        vehicleListAdapter = AdminVehicleListAdapter(requireContext(),mVehicleViewModel)
        binding.recyclerViewVehiclesAdmin.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVehiclesAdmin.adapter = vehicleListAdapter

        mVehicleViewModel.readAllData.observe(requireActivity(), { vehicle ->
            vehicleListAdapter.setData(vehicle)
            //vehicleList = vehicle
        })

        val finishedCount = sharedPref.getInt("finished",0)
        val resetedCount = sharedPref.getInt("reseted",0)

        val finished = "Bitiş Sayısı: $finishedCount"
        val reseted = "Reset Sayısı: $resetedCount"
        val total = "Toplam Kullanım: ${(finishedCount + resetedCount)}"

        binding.textViewNumberOfFinish.text = finished
        binding.textViewNumberOfReset.text = reseted
        binding.textViewTotalUsage.text = total

        binding.buttonClearPast.setOnClickListener {
            deletePast()
        }

    }

    private fun deletePast(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes){ _, _ ->
            mPastUsesViewModel.deletePastUses()
            val editor = sharedPref.edit()
            editor.putInt("finished",0)
            editor.putInt("reseted",0)
            editor.apply()

            val finishedCount = sharedPref.getInt("finished",0)
            val resetedCount = sharedPref.getInt("reseted",0)

            val finished = "Bitiş Sayısı: $finishedCount"
            val reseted = "Reset Sayısı: $resetedCount"
            val total = "Toplam Kullanım: ${(finishedCount + resetedCount)}"

            binding.textViewNumberOfFinish.text = finished
            binding.textViewNumberOfReset.text = reseted
            binding.textViewTotalUsage.text = total

            Toast.makeText(requireContext(), R.string.past_deleted, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.no){ _, _ -> }
        builder.setTitle(R.string.delete_past)
        builder.setMessage(R.string.are_you_sure_delete_past)
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}