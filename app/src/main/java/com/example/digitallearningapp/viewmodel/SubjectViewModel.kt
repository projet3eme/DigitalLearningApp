package com.example.digitallearningapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitallearningapp.data.repository.YouTubeRepository
import com.example.digitallearningapp.model.Subject
import kotlinx.coroutines.launch

class SubjectViewModel : ViewModel() {

    private val repository = YouTubeRepository()

    private val _subjects = mutableStateOf<List<Subject>>(emptyList())
    val subjects: State<List<Subject>> = _subjects

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun fetchSubjects(level: String, year: String) {
        viewModelScope.launch {
            _isLoading.value = true
            android.util.Log.d("SUBJECT_VM", "fetchSubjects called: level=$level, year=$year")
            _subjects.value = repository.getSubjects(level, year)
            android.util.Log.d("SUBJECT_VM", "subjects count: ${_subjects.value.size}")
            _isLoading.value = false
        }
    }
}
