package com.example.schedule.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.schedule.R
import com.example.schedule.databinding.FragmentLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding: FragmentLogInBinding
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
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            logButton.setOnClickListener {
                if (emailInput.text.toString().isEmpty() || passwordInput.text.toString().isEmpty()) {
                    val message = Toast.makeText(context, getString(R.string.warning_message), Toast.LENGTH_LONG)
                    message.show()
                } else {
                    val email = emailInput.text.toString()
                    val password = passwordInput.text.toString()

                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

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