package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "simulation_results")
data class SimulationResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientName: String,
    val clinicalSummary: String,
    val selectedCondition: String,
    val perceptionOutput: String,
    val reasoningOutput: String,
    val safetyCheck: String,
    val conclusion: String,
    val timestamp: Long = System.currentTimeMillis()
)
