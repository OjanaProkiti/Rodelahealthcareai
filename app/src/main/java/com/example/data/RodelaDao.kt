package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RodelaDao {
    // Research Notes queries
    @Query("SELECT * FROM research_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<ResearchNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ResearchNote)

    @Update
    suspend fun updateNote(note: ResearchNote)

    @Delete
    suspend fun deleteNote(note: ResearchNote)

    // Simulation Results queries
    @Query("SELECT * FROM simulation_results ORDER BY timestamp DESC")
    fun getAllSimulations(): Flow<List<SimulationResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSimulation(simulation: SimulationResult)

    @Query("DELETE FROM simulation_results WHERE id = :id")
    suspend fun deleteSimulationById(id: Int)
}
