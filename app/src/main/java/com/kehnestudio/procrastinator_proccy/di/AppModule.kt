package com.kehnestudio.procrastinator_proccy.di

import android.app.Application
import androidx.annotation.Nullable
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kehnestudio.procrastinator_proccy.data.offline.UserDao
import com.kehnestudio.procrastinator_proccy.data.offline.UserDatabase
import com.kehnestudio.procrastinator_proccy.repositories.FireStoreRepository
import com.kehnestudio.procrastinator_proccy.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ) =
        Room.databaseBuilder(app, UserDatabase::class.java, "user_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(db: UserDatabase) = db.userDao()

    @Provides
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository{
        return UserRepository(userDao)
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideProductsCollectionReference(rootRef: FirebaseFirestore): CollectionReference {
        return rootRef.collection("user_collection")
    }

    @Nullable
    @Provides
    fun provideFireStoreRepository(
        userRepository: UserRepository,
        rootRef: CollectionReference,
        firebaseAuth: FirebaseAuth
    ):FireStoreRepository{
        return FireStoreRepository(userRepository,rootRef, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    //Coroutine lives as long as application because it is a Singleton and this Module is Installed Application Component
    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope