package farees.hussain.shareify.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import farees.hussain.shareify.data.local.ShareifyDatabase
import farees.hussain.shareify.data.remote.ShareifyAPI
import farees.hussain.shareify.other.Constants.BASE_URL_UPLOAD
import farees.hussain.shareify.other.Constants.SHAREIFY_DATABASE_NAME
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideShareifyDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShareifyDatabase::class.java, SHAREIFY_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShareifyDatabase
    ) = database.shareifyDao()

    @Singleton
    @Provides
    fun providesShareifyAPI(): ShareifyAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_UPLOAD)
            .build()
            .create(ShareifyAPI::class.java)
    }

}