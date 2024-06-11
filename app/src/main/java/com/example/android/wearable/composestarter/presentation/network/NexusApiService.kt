package com.example.android.wearable.composestarter.presentation.network
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.http.GET
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.internal.platform.Platform
import retrofit2.http.Path

private const val BASE_URL =
    "https://metro-rti.nexus.org.uk/api/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface NexusApiService{
    @GET("stations")
    suspend fun getStations(): Map<String, String>
    @GET("stations/platforms")
    suspend fun getPlatforms(): Map<String,List<PlatformInfo>>
    @GET("times/{station}/{platform}")
    suspend fun getTimes(@Path("station") station: String, @Path("platform") platform: String): List<TimeInfo>
}

object NexusApi {
    val retrofitService: NexusApiService by lazy {
        retrofit.create(NexusApiService::class.java)
    }
}
