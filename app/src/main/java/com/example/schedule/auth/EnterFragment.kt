package com.example.schedule.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentEnterBinding

class EnterFragment : Fragment() {

    private var _binding: FragmentEnterBinding? = null
    private val binding: FragmentEnterBinding
        get() = checkNotNull(_binding)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            logUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_toLogUp)
            }

            logInButton.setOnClickListener {
                findNavController().navigate(R.id.action_toLogIn)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}