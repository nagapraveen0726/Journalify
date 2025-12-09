package uk.ac.tees.mad.journalify.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import uk.ac.tees.mad.journalify.domain.usecase.SyncPushUseCase

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncPush: SyncPushUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            syncPush()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
