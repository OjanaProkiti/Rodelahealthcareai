package com.example.data

import kotlinx.coroutines.flow.Flow

class RodelaRepository(private val dao: RodelaDao) {
    val allNotes: Flow<List<ResearchNote>> = dao.getAllNotes()
    val allSimulations: Flow<List<SimulationResult>> = dao.getAllSimulations()

    suspend fun insertNote(note: ResearchNote) {
        dao.insertNote(note)
    }

    suspend fun updateNote(note: ResearchNote) {
        dao.updateNote(note)
    }

    suspend fun deleteNote(note: ResearchNote) {
        dao.deleteNote(note)
    }

    suspend fun insertSimulation(simulation: SimulationResult) {
        dao.insertSimulation(simulation)
    }

    suspend fun deleteSimulationById(id: Int) {
        dao.deleteSimulationById(id)
    }
}
