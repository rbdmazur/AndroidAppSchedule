package com.example.schedule.fragments.profilefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.R
import com.example.schedule.databinding.FragmentProfileBinding
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.fragments.schedulefragment.ScheduleViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = checkNotNull(_binding)

    private val scheduleRepository = ScheduleRepository.get()
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var schedulesAdapter: ProfileScheduleAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    private fun initSchedulesAdapter() {
        schedulesAdapter = ProfileScheduleAdapter(
            viewModel.checkedScheduleIndex,
            addScheduleListener
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