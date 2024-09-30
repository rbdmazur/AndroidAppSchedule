package com.example.schedule.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentRegBinding
import com.google.firebase.auth.FirebaseAuth


class LogUpFragment : Fragment() {

    private var _binding: FragmentRegBinding? = null
    private val binding: FragmentRegBinding
        get() = checkNotNull(_binding)

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            regButton.setOnClickListener {
                if (nameInput.text.toString().isEmpty() ||
                    surnameInput.text.toString().isEmpty() ||
                    emailInput.text.toString().isEmpty() ||
                    passwordInput.text.toString().isEmpty()
                    ) {
                    val message = Toast.makeText(context, getString(R.string.warning_message), Toast.LENGTH_LONG)
                    message.show()
                } else {
                    val email = emailInput.text.toString()
                    val password = passwordInput.text.toString()
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                findNavController().navigate(R.id.action_logUp_to_logIn)
                            } else {
                                val message = Toast.makeText(context, getString(R.string.smth_went_wrong), Toast.LENGTH_LONG)
                                message.show()
                            }
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}