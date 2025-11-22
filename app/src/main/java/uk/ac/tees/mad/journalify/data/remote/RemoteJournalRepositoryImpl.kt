package uk.ac.tees.mad.journalify.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.journalify.domain.model.CloudJournalEntry
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import javax.inject.Inject

class RemoteJournalRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore
) : RemoteJournalRepository {
    private fun userRef() =

        store.collection("users")
            .document(auth.currentUser?.uid ?: "INVALID")
            .collection("journal")


    override suspend fun push(entry: CloudJournalEntry) {
        val doc = if(entry.id.isBlank())
            userRef().document()
        else
            userRef().document(entry.id)

        doc.set(entry.copy(id = doc.id)).await()
    }

    override suspend fun pullAll(): List<CloudJournalEntry> {
        return userRef()
            .get()
            .await()
            .toObjects(CloudJournalEntry::class.java)
    }

    override suspend fun delete(id: String) {
        userRef()
            .document(id)
            .delete()
            .await()
    }

}