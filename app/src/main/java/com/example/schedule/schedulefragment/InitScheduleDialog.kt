package com.example.schedule.schedulefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.CreateScheduleTitleBinding
import com.example.schedule.model.Schedule
import com.example.schedule.model.Subject
import com.example.schedule.repositories.SUBJECT_TAG
import com.example.schedule.repositories.ScheduleRepository
import com.example.schedule.repositories.SubjectsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class InitScheduleDialog : DialogFragment() {
    private val scheduleRepository = ScheduleRepository.get()
    private val subjectsRepository = SubjectsRepository.get()

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
        Log.d(SUBJECT_TAG, "From SubjectsRep: ${subjectsRepository.getSubjects()}")
        Log.d("Sch_id", "$schedule")
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

                Log.d("Sch_id", "$schedule")
                findNavController().navigate(InitScheduleDialogDirections.actionInitToCreate(schedule.id))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}