package com.zafertugcu.araczamanlamasistemi.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.adapter.AdminVehicleListAdapter
import com.zafertugcu.araczamanlamasistemi.adapter.PastUsesAdapter
import com.zafertugcu.araczamanlamasistemi.adapter.VehicleAdapter
import com.zafertugcu.araczamanlamasistemi.databinding.ChangePasswordBinding
import com.zafertugcu.araczamanlamasistemi.databinding.DialogAddVehicleBinding
import com.zafertugcu.araczamanlamasistemi.databinding.FragmentAdminBinding
import com.zafertugcu.araczamanlamasistemi.model.VehicleInfoModel
import com.zafertugcu.araczamanlamasistemi.view.activity.MainActivity
import com.zafertugcu.araczamanlamasistemi.viewmodel.PastUsesViewModel
import com.zafertugcu.araczamanlamasistemi.viewmodel.VehicleViewModel

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var vehicleListAdapter: AdminVehicleListAdapter
    private lateinit var mVehicleViewModel: VehicleViewModel
    private lateinit var pastUsesAdapter: PastUsesAdapter
    private lateinit var mPastUsesViewModel: PastUsesViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefTimer: SharedPreferences
    private lateinit var spinnerColors: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater,container,false)
        val view = binding.root
        (activity as MainActivity?)?.setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("password", Context.MODE_PRIVATE)
        sharedPrefTimer = requireActivity().getSharedPreferences(getString(R.string.past_shared), Context.MODE_PRIVATE)
        spinnerColors = resources.getStringArray(R.array.colors)

        mVehicleViewModel = ViewModelProvider(requireActivity()).get(VehicleViewModel::class.java)
        mPastUsesViewModel = ViewModelProvider(requireActivity()).get(PastUsesViewModel::class.java)

        vehicleListAdapter = AdminVehicleListAdapter(requireContext(),mVehicleViewModel)
        binding.recyclerViewVehiclesAdmin.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVehiclesAdmin.adapter = vehicleListAdapter

        pastUsesAdapter = PastUsesAdapter(requireContext())
        binding.recyclerViewPastUses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPastUses.adapter = pastUsesAdapter

        mVehicleViewModel.readAllData.observe(requireActivity(), { vehicle ->
            vehicleListAdapter.setData(vehicle)
        })

        mPastUsesViewModel.readAllData.observe(requireActivity(), { pastUses ->
            pastUsesAdapter.setData(pastUses)
        })

        val finishedCount = sharedPrefTimer.getInt("finished",0)
        val resetedCount = sharedPrefTimer.getInt("reseted",0)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.vehicle_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionAddVehicle -> {
                addVehicleDialog()
            }
            R.id.actionDeleteAll -> {
                deleteAllData()
            }
            R.id.actionChangePassword -> {
                changePasswordDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changePasswordDialog(){
        val dialog = Dialog(requireContext())
        val dialogPasswordBinding = ChangePasswordBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogPasswordBinding.root)

        val oldPassword = sharedPref.getString("login_password","1234")

        dialogPasswordBinding.buttonConfirm.setOnClickListener {
            if(dialogPasswordBinding.editTextOldPass.text.isNotEmpty()
                && dialogPasswordBinding.editTextNewPass.text.isNotEmpty()){
                val newPassword = dialogPasswordBinding.editTextNewPass.text.toString()
                if(oldPassword == dialogPasswordBinding.editTextOldPass.text.toString()){
                    val editor = sharedPref.edit()
                    editor.putString("login_password",newPassword)
                    editor.apply()
                    Toast.makeText(requireContext(),R.string.password_changed,Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(),R.string.password_error,Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(),R.string.fill_in_the_blanks,Toast.LENGTH_SHORT).show()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun addVehicleDialog(){
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogAddVehicleBinding
            .inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,spinnerColors)
        dialogBinding.spinner.adapter = spinnerAdapter
        dialogBinding.buttonSaveVehicle.setOnClickListener {
            val vehicleName = dialogBinding.editTextVehicleName.text
            val vehicleTime = dialogBinding.editTextVehicleTime.text
            if (vehicleName.isNotEmpty() && vehicleTime.isNotEmpty()){
                val color = dialogBinding.spinner.selectedItem.toString()
                insertDataToDatabase(vehicleName.toString(), vehicleTime.toString().toInt(),color)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), R.string.fill_in_the_blanks,Toast.LENGTH_SHORT).show()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun insertDataToDatabase(vehicleName: String, vehicleTime: Int, vehicleColor: String){
        val vehicle = VehicleInfoModel(
            0,
            vehicleName,
            vehicleTime,
            vehicleTime,
            vehicleColor,
            false,
            2
        )
        mVehicleViewModel.addVehicle(vehicle)
        Toast.makeText(requireContext(), R.string.save_is_successful,Toast.LENGTH_SHORT).show()
    }

    private fun deleteAllData(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes){ _,_ ->
            mVehicleViewModel.deleteAllVehicle()
            Toast.makeText(requireContext(), R.string.all_deleted, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.no){ _, _ -> }
        builder.setTitle(R.string.delete_all)
        builder.setMessage(R.string.are_you_sure_delete_all)
        builder.create().show()
    }

    private fun deletePast(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes){ _, _ ->
            mPastUsesViewModel.deletePastUses()
            val editor = sharedPrefTimer.edit()
            editor.putInt("finished",0)
            editor.putInt("reseted",0)
            editor.apply()

            val finishedCount = sharedPrefTimer.getInt("finished",0)
            val resetedCount = sharedPrefTimer.getInt("reseted",0)

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