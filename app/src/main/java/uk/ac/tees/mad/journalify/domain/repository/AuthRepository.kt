package uk.ac.tees.mad.journalify.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun signup(email: String, password: String)
    suspend fun reset(email: String)
}