package uk.ac.tees.mad.journalify.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.journalify.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signup(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun reset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override fun currentUid(): String? {
        return auth.currentUser?.uid
    }
}