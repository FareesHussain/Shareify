package farees.hussain.shareify.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import farees.hussain.shareify.data.local.ShareifyDatabase
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDao(@ApplicationContext context : Context) =
        Room.inMemoryDatabaseBuilder(context, ShareifyDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}