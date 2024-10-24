package com.example.schedule.startfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentStartBinding
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch


class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding: FragmentStartBinding
        get() = checkNotNull(_binding)
    private val scheduleRepository = ScheduleRepository.get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startButton.setOnClickListener {
//            findNavController().navigate(R.id.action_toEnter)
            viewLifecycleOwner.lifecycleScope.launch {
                val schedules = scheduleRepository.getSchedules()
                val schedule = if (schedules.isEmpty()) null else schedules[0]
                findNavController().navigate(StartFragmentDirections.actionStartToSchedule(schedule?.id))
            }
//            findNavController().navigate(StartFragmentDirections.actionStartToSchedule(null))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}