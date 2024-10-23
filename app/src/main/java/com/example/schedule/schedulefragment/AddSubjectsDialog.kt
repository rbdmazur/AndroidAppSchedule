package com.example.schedule.schedulefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.schedule.R
import com.example.schedule.databinding.AddSubjectsDialogBinding
import com.example.schedule.model.Subject
import com.example.schedule.repositories.ScheduleRepository
import kotlinx.coroutines.launch
import java.util.UUID

private const val counterKey = "counter"

class AddSubjectsDialog : DialogFragment() {

    private var _binding: AddSubjectsDialogBinding? = null
    private val binding: AddSubjectsDialogBinding
        get() = checkNotNull(_binding)
    private var counter = 0
    private val scheduleRepository = ScheduleRepository.get()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddSubjectsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.subjectsCounter.text = getString(R.string.subjects_counter, counter)

        binding.subjectsAddButton.setOnClickListener {
            if (binding.subjectEditText.text.isEmpty()) {
                Toast.makeText(this.context, getString(R.string.subjects_warning_blank), Toast.LENGTH_LONG).show()
            } else {
                lifecycleScope.launch {
                    val subjectName = binding.subjectEditText.text.toString()
                    addSubjectToDatabase(subjectName)
                }
                counter++
                binding.subjectEditText.text.clear()
                binding.subjectsCounter.text = getString(R.string.subjects_counter, counter)
            }
        }

        binding.subjectsCancelButton.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(counterKey, counter)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        counter = savedInstanceState?.getInt(counterKey) ?: 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private suspend fun addSubjectToDatabase(subjectName: String) {
        val subject = Subject(UUID.randomUUID(), subjectName)
        scheduleRepository.addSubject(subject)
    }
}