package ru.kpfu.itis.debugmenu.feature.network.provider

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.kpfu.itis.debugmenu.BuildConfig
import ru.kpfu.itis.debugmenu.base.EnvironmentRepository
import ru.kpfu.itis.debugmenu.feature.network.api.TestApi
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal fun provideChucker(context: Context): ChuckerInterceptor =
    ChuckerInterceptor.Builder(context)
        .collector(
            ChuckerCollector(
                context = context,
                showNotification = true,
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )
        )
        .maxContentLength(1024 * 1024 * 256)
        .alwaysReadResponseBody(true)
        .build()

internal fun provideOkHttpClient(
    chucker: ChuckerInterceptor
): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(30L, TimeUnit.SECONDS)
        .readTimeout(30L, TimeUnit.SECONDS)
        .writeTimeout(30L, TimeUnit.SECONDS)
        .apply {
            if (listOf("debug", "stage").contains(BuildConfig.BUILD_TYPE)) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor { message ->
                        Timber.tag("OkHttp").d(message)
                    }.setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                addNetworkInterceptor(chucker)
            }
        }
        .build()

internal fun provideRetrofit(
    okHttpClient: OkHttpClient,
    json: Json,
    environmentRepository: EnvironmentRepository
): Retrofit = Retrofit.Builder()
    .baseUrl(environmentRepository.getServerBaseUrl())
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

internal fun provideJson() = Json {
    ignoreUnknownKeys = true
}

internal fun provideApi(retrofit: Retrofit): TestApi = retrofit.create(TestApi::class.java)