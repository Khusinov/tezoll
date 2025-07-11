package uz.khusinov.karvon.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.khusinov.karvon.SharedPref
 import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.utills.Constants.BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context) = SharedPref(context)

//    @Provides
//    @Singleton
//    fun provideFuseClient(@ApplicationContext context: Context) =
//        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideFuseClient(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    @Named("socket")
    fun provideOkHttpClient(sharedPref: SharedPref): OkHttpClient = with(OkHttpClient.Builder()) {
        readTimeout(1, TimeUnit.SECONDS)
        writeTimeout(1, TimeUnit.SECONDS)
        connectTimeout(1, TimeUnit.SECONDS)
        addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            if (sharedPref.access != "") {
                builder.header("token", sharedPref.access)
            }
            chain.proceed(builder.build())
        }).build()
    }


    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun getClient(sharedPref: SharedPref): OkHttpClient = with(
        OkHttpClient.Builder()
    ) {
        connectTimeout(60, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        // addInterceptor(ChuckerInterceptor(context))

        addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()

            if (sharedPref.access != "") {
                builder.header("Authorization", "Bearer ${sharedPref.access}")
            }
            builder.header("Accept", "application/json")
            chain.proceed(builder.build())
        }).build()

    }
}