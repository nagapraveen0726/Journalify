package uk.ac.tees.mad.journalify.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import uk.ac.tees.mad.journalify.data.local.AppDatabase
import uk.ac.tees.mad.journalify.data.local.dao.JournalEntryDao
import uk.ac.tees.mad.journalify.data.remote.RemoteJournalRepositoryImpl
import uk.ac.tees.mad.journalify.data.repository.AuthRepositoryImpl
import uk.ac.tees.mad.journalify.data.repository.LocalJournalRepositoryImpl
import uk.ac.tees.mad.journalify.data.session.SessionManager
import uk.ac.tees.mad.journalify.domain.repository.AuthRepository
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import uk.ac.tees.mad.journalify.util.ConnectivityObserver
import uk.ac.tees.mad.journalify.util.NetworkConnectivityObserver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Firebase
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    // Session Manager (DataStore)
    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager = SessionManager(context)

    // Connectivity Observer
    @Singleton
    @Provides
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ): ConnectivityObserver = NetworkConnectivityObserver(context)

    // Dispatchers
    @Provides
    fun provideDispatcherIO() = Dispatchers.IO

    @Provides
    fun provideDispatcherMain() = Dispatchers.Main

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            "journalify.db"
        ).build()

    @Provides
    fun provideJournalDao(db: AppDatabase): JournalEntryDao =
        db.journalEntryDao()

    @Provides
    @Singleton
    fun provideLocalRepo(
        dao: JournalEntryDao
    ): LocalJournalRepository = LocalJournalRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideRemoteJournalRepository(
        auth: FirebaseAuth,
        store: FirebaseFirestore
    ): RemoteJournalRepository = RemoteJournalRepositoryImpl(auth, store)
}
