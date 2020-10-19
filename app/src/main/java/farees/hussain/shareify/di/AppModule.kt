package farees.hussain.shareify.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import farees.hussain.shareify.data.local.ShareifyDao
import farees.hussain.shareify.data.local.ShareifyDatabase
import farees.hussain.shareify.data.remote.ShareifyAPI
import farees.hussain.shareify.other.Constants.BASE_URL_UPLOAD
import farees.hussain.shareify.other.Constants.SHAREIFY_DATABASE_NAME
import farees.hussain.shareify.repositories.DefaultShareifyRepository
import farees.hussain.shareify.repositories.ShareifyRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(1000000, TimeUnit.SECONDS)
            .readTimeout(1000000, TimeUnit.SECONDS)
            .writeTimeout(1000000, TimeUnit.SECONDS)
            .callTimeout(1000000, TimeUnit.SECONDS)
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_UPLOAD)
            .client(client.build())
            .build()
            .create(ShareifyAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesDefaultShareifyRepository(
        dao: ShareifyDao,
        api: ShareifyAPI
    ) = DefaultShareifyRepository(dao, api) as ShareifyRepository

}