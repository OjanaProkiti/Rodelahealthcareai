package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RodelaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RodelaRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = RodelaRepository(database.rodelaDao())
    }

    // Database Observables
    private val _notes = MutableStateFlow<List<ResearchNote>>(emptyList())
    val notes: StateFlow<List<ResearchNote>> = _notes.asStateFlow()

    private val _savedSimulations = MutableStateFlow<List<SimulationResult>>(emptyList())
    val savedSimulations: StateFlow<List<SimulationResult>> = _savedSimulations.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allNotes.collectLatest {
                _notes.value = it
            }
        }
        viewModelScope.launch {
            repository.allSimulations.collectLatest {
                _savedSimulations.value = it
            }
        }
    }

    // --- Interactive Simulation State ---
    private val _patientName = MutableStateFlow("John Doe")
    val patientName: StateFlow<String> = _patientName.asStateFlow()

    private val _clinicalNotes = MutableStateFlow(
        "Patient is a 54yo female, Type 2 diabetes for 12 years. Last HbA1c is 8.4%. Fundus photos of the left eye show multiple microaneurysms, mild intraretinal hemorrhages, and hard exudates in the macular region. No neovascularization."
    )
    val clinicalNotes: StateFlow<String> = _clinicalNotes.asStateFlow()

    private val _selectedCondition = MutableStateFlow("Diabetic Retinopathy Screen")
    val selectedCondition: StateFlow<String> = _selectedCondition.asStateFlow()

    // Simulation running indicators
    data class DiagnosticProgress(
        val isRunning: Boolean = false,
        val currentAgent: String = "", // "Perception", "Reasoning", "Safety", "Conclusion", "Done"
        val progressPercent: Float = 0f,
        val perceptionOutput: String = "",
        val reasoningOutput: String = "",
        val safetyCheck: String = "",
        val conclusion: String = ""
    )

    private val _simulationState = MutableStateFlow(DiagnosticProgress())
    val simulationState: StateFlow<DiagnosticProgress> = _simulationState.asStateFlow()

    fun updatePatientName(name: String) {
        _patientName.value = name
    }

    fun updateClinicalNotes(notes: String) {
        _clinicalNotes.value = notes
    }

    fun updateSelectedCondition(condition: String) {
        _selectedCondition.value = condition
        // Populate standard notes automatically for ease of testing
        _clinicalNotes.value = when {
            condition.contains("Retinopathy") -> "Patient is a 54yo female, Type 2 diabetes for 12 years. Last HbA1c is 8.4%. Fundus photos of the left eye show multiple microaneurysms, mild intraretinal hemorrhages, and hard exudates in the macular region. No neovascularization."
            condition.contains("Melanoma") -> "Patient is a 38yo male, fair-skinned. Presents with a pigmented lesion on the right upper back. The lesion is asymmetrical, has irregular borders, variable color (shades of dark brown and black), diameter is 7mm, and has evolved over the last 3 months."
            condition.contains("Sepsis") -> "Patient is a 72yo male post-op Day 2 total hip arthroplasty. Temperature is 38.9°C, Heart Rate is 112 bpm, Respiratory Rate is 24/min, Blood Pressure is 95/55 mmHg. WBC count is 16.5 x 10^9/L. Confused and lethargic."
            condition.contains("Cardiovascular") -> "Patient is a 62yo male, history of hypertension and hyperlipidemia. Presents with acute-onset substernal chest pressure, radiating to the left arm and jaw, accompanied by diaphoresis and shortness of breath. Duration: 45 minutes. ECG shows 2mm ST-segment elevation in leads V1-V4."
            else -> ""
        }
        _patientName.value = when {
            condition.contains("Retinopathy") -> "Eleanor Vance"
            condition.contains("Melanoma") -> "Marcus Brody"
            condition.contains("Sepsis") -> "Arthur Pendelton"
            condition.contains("Cardiovascular") -> "Robert Vance"
            else -> "Anonymous Patient"
        }
    }

    fun runDiagnosticSimulation() {
        viewModelScope.launch {
            _simulationState.value = DiagnosticProgress(
                isRunning = true,
                currentAgent = "Perception Agent (EHR Signal Extraction)",
                progressPercent = 0.1f
            )

            // Step 1: Simulate Perception Agent
            delay(1200)
            val result = GeminiService.analyzeCase(
                patientName = _patientName.value,
                clinicalSummary = _clinicalNotes.value,
                selectedCondition = _selectedCondition.value
            )

            _simulationState.value = _simulationState.value.copy(
                progressPercent = 0.35f,
                currentAgent = "Reasoning Agent (Zero-Shot Medical Mapping)",
                perceptionOutput = result.perception
            )

            // Step 2: Simulate Reasoning Agent
            delay(1500)
            _simulationState.value = _simulationState.value.copy(
                progressPercent = 0.65f,
                currentAgent = "Safety Agent (Pharmacological safety checks)",
                reasoningOutput = result.reasoning
            )

            // Step 3: Simulate Safety Agent
            delay(1200)
            _simulationState.value = _simulationState.value.copy(
                progressPercent = 0.85f,
                currentAgent = "Explainability Agent (Conclusion Synthesis & Transparency)",
                safetyCheck = result.safety
            )

            // Step 4: Simulate Final Conclusion Agent
            delay(1000)
            _simulationState.value = _simulationState.value.copy(
                isRunning = false,
                currentAgent = "Diagnostic Cycle Completed Successfully",
                progressPercent = 1.0f,
                conclusion = result.conclusion
            )

            // Auto-save to Database History
            val simulationResult = SimulationResult(
                patientName = _patientName.value,
                clinicalSummary = _clinicalNotes.value,
                selectedCondition = _selectedCondition.value,
                perceptionOutput = _simulationState.value.perceptionOutput,
                reasoningOutput = _simulationState.value.reasoningOutput,
                safetyCheck = _simulationState.value.safetyCheck,
                conclusion = _simulationState.value.conclusion
            )
            repository.insertSimulation(simulationResult)
        }
    }

    fun clearSimulation() {
        _simulationState.value = DiagnosticProgress()
    }

    fun deleteSimulation(id: Int) {
        viewModelScope.launch {
            repository.deleteSimulationById(id)
        }
    }

    // --- Research Notes Methods ---
    fun addNote(title: String, content: String, category: String) {
        viewModelScope.launch {
            repository.insertNote(ResearchNote(title = title, content = content, category = category))
        }
    }

    fun updateNote(note: ResearchNote) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: ResearchNote) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    // --- Podcast Player State & Simulation ---
    data class PodcastChapter(
        val title: String,
        val durationSeconds: Int,
        val speaker: String,
        val transcript: String
    )

    val chapters = listOf(
        PodcastChapter(
            title = "Episode 1: Zero-Shot Diagnostics",
            durationSeconds = 64,
            speaker = "Dr. Palash Md. Kamal Uddin (Arshan Solution)",
            transcript = "Welcome to our special clinical podcast. Traditional AI models require immense quantities of custom-labeled datasets to excel at specific tasks. In contrast, the Rodela Health Care Engine v2 leverages deep medical foundation models to execute zero-shot reasoning. This means the engine holds an inherent semantic map of general clinical medicine, pathology, and therapeutics, enabling it to forecast patient trends and construct sound diagnoses for novel cases out of the box."
        ),
        PodcastChapter(
            title = "Episode 2: The Agentic Shield Architecture",
            durationSeconds = 88,
            speaker = "Dr. Palash Md. Kamal Uddin (Arshan Solution)",
            transcript = "A major barrier to clinical adoption of AI is the black-box effect. If a doctor receives a prediction without context, it breeds distrust. For Rodela v2, we created an ecosystem of modular clinical agents. Our Perception Agent parses EHR notes to extract signs. Our Reasoning Agent maps these findings to clinical guidelines. Crucially, our Pharmacological Safety Agent screens contraindications and dose limits. This multi-layered architecture ensures transparency and auditable logic chains."
        ),
        PodcastChapter(
            title = "Episode 3: Cost-Efficiency & Future Readiness",
            durationSeconds = 72,
            speaker = "Dr. Palash Md. Kamal Uddin (Arshan Solution)",
            transcript = "Compute economics in 2025 shifted the medical AI landscape completely. We experienced a staggering 280-fold reduction in compute-native inference costs. This allows hospital systems to deploy Rodela locally on existing infrastructure or private clouds without expensive dedicated server clusters. This accessibility enables rapid, massive scaling, bringing specialized, high-sensitivity diagnostics to rural clinics, underfunded hospitals, and remote telemedicine settings globally."
        )
    )

    private val _currentChapterIndex = MutableStateFlow(0)
    val currentChapterIndex: StateFlow<Int> = _currentChapterIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgressSeconds = MutableStateFlow(0)
    val playbackProgressSeconds: StateFlow<Int> = _playbackProgressSeconds.asStateFlow()

    private var podcastJob: Job? = null

    fun selectChapter(index: Int) {
        _currentChapterIndex.value = index
        _playbackProgressSeconds.value = 0
        val wasPlaying = _isPlaying.value
        if (wasPlaying) {
            startPodcastTimer()
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            _isPlaying.value = false
            podcastJob?.cancel()
        } else {
            _isPlaying.value = true
            startPodcastTimer()
        }
    }

    private fun startPodcastTimer() {
        podcastJob?.cancel()
        podcastJob = viewModelScope.launch {
            val maxDuration = chapters[_currentChapterIndex.value].durationSeconds
            while (_isPlaying.value && _playbackProgressSeconds.value < maxDuration) {
                delay(1000)
                _playbackProgressSeconds.value += 1
                if (_playbackProgressSeconds.value >= maxDuration) {
                    // Autoplay next chapter if available
                    if (_currentChapterIndex.value < chapters.lastIndex) {
                        _currentChapterIndex.value += 1
                        _playbackProgressSeconds.value = 0
                    } else {
                        // End of playlist
                        _isPlaying.value = false
                        _playbackProgressSeconds.value = 0
                        break
                    }
                }
            }
        }
    }

    fun skipForward() {
        val maxDuration = chapters[_currentChapterIndex.value].durationSeconds
        _playbackProgressSeconds.value = (_playbackProgressSeconds.value + 10).coerceAtMost(maxDuration)
    }

    fun skipBackward() {
        _playbackProgressSeconds.value = (_playbackProgressSeconds.value - 10).coerceAtLeast(0)
    }

    override fun onCleared() {
        super.onCleared()
        podcastJob?.cancel()
    }
}

class RodelaViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RodelaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RodelaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
