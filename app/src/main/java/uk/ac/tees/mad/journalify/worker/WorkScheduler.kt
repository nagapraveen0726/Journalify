package uk.ac.tees.mad.journalify.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkScheduler {

    private const val SYNC_WORK = "sync_work"

    fun schedule(context: Context) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                20,
                TimeUnit.SECONDS
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                SYNC_WORK,
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}
