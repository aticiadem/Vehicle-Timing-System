package com.zafertugcu.araczamanlamasistemi.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.zafertugcu.araczamanlamasistemi.R
import com.zafertugcu.araczamanlamasistemi.databinding.DialogLogInBinding
import com.zafertugcu.araczamanlamasistemi.databinding.FragmentChoiceBinding

class ChoiceFragment : Fragment() {

    private var _binding: FragmentChoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoiceBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("password", Context.MODE_PRIVATE)

        binding.buttonGoAdminFragment.setOnClickListener {
            showLogInDialog()
        }

        binding.buttonGoUserFragment.setOnClickListener {
            findNavController().navigate(R.id.action_choiceFragment_to_userFragment)
        }

    }

    private fun showLogInDialog(){
        val dialog = Dialog(requireContext())
        val loginBinding = DialogLogInBinding
            .inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(loginBinding.root)
        loginBinding.buttonLogIn.setOnClickListener {
            val password = sharedPref.getString("login_password","1234")

            if(loginBinding.editTextLoginPass.text.isNotEmpty()){
                if(loginBinding.editTextLoginPass.text.toString() == password){
                    findNavController().navigate(R.id.action_choiceFragment_to_adminFragment)
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), R.string.password_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.fill_in_the_blanks, Toast.LENGTH_SHORT).show()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}