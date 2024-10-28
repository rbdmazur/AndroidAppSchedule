package com.example.schedule.schedulefragment.createschedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.CreateScheduleTitleBinding
import com.example.schedule.model.Schedule
import com.example.schedule.repositories.SUBJECT_TAG
import com.example.schedule.repositories.ScheduleRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.UUID

class InitScheduleDialog : DialogFragment() {
    private val scheduleRepository = ScheduleRepository.get()

    private var schedule = Schedule(UUID.randomUUID(), "")

    private var _binding: CreateScheduleTitleBinding? = null
    private val binding: CreateScheduleTitleBinding
        get() = checkNotNull(_binding)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateScheduleTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e(SUBJECT_TAG, "Exception catched", exception)
        }
        binding.createScheduleButton.setOnClickListener {
            if (binding.scheduleTitleEditText.text.isBlank()) {
                Toast.makeText(context, resources.getString(R.string.toast_warning), Toast.LENGTH_LONG)
                    .show()
            } else {
                val title = binding.scheduleTitleEditText.text.toString()
                schedule = schedule.copy(title = title)
                lifecycleScope.launch(exceptionHandler) {
                    scheduleRepository.addSchedule(schedule)
                }

                findNavController().navigate(
                    InitScheduleDialogDirections.actionInitToCreate(schedule.id)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}