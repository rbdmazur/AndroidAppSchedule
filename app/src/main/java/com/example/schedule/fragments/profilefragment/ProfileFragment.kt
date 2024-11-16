package com.example.schedule.fragments.profilefragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.MainActivity
import com.example.schedule.databinding.FragmentProfileBinding
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.ScheduleViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = checkNotNull(_binding)

    private val scheduleRepository = ScheduleRepository.get()
    private lateinit var viewModel: ScheduleViewModel
    private lateinit var schedulesAdapter: ProfileScheduleAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as MainActivity).viewModel
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.schedulesListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSchedulesAdapter()
        observeSchedules()
        binding.schedulesListRecyclerView.adapter = schedulesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val addScheduleListener: () -> Unit = {
        findNavController().navigate(ProfileFragmentDirections.actionProfileToInit())
    }

    private val changeSchedule: (position: Int) -> Unit = { position ->
        viewLifecycleOwner.lifecycleScope.launch {
            val schedule = scheduleRepository.getSchedules()[position]
            viewModel.updateScheduleId(schedule.id, position)
            Log.d("ViewModelChange", "${schedule.id}, $position")
        }
    }

    private fun initSchedulesAdapter() {
        schedulesAdapter = ProfileScheduleAdapter(
            viewModel.checkedScheduleIndex,
            addScheduleListener,
            changeSchedule
        )
    }

    private fun observeSchedules() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                schedulesAdapter.diffList.submitList(viewModel.scheduleRepository.getSchedules())
            }
        }
    }
}