package com.samrobbins.android.wearable.metrotimes.network

import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {
    private var token: String? = null
    private val tokenUrl = "https://ken.nebulalabs.cc/realtime/token/"

    private val json = Json { ignoreUnknownKeys = true }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth for the token endpoint itself if we were using the same client
        // but here we use a separate one or just call it directly.
        if (originalRequest.url.toString() == tokenUrl) {
            return chain.proceed(originalRequest)
        }

        synchronized(this) {
            if (token == null) {
                token = fetchToken()
            }
        }

        val authenticatedRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        var response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            synchronized(this) {
                token = fetchToken()
            }
            if (token != null) {
                response.close()
                val retryRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                response = chain.proceed(retryRequest)
            }
        }

        return response
    }

    private fun fetchToken(): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(tokenUrl)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val body = response.body?.string() ?: return null
                val tokenResponse = json.decodeFromString<TokenResponse>(body)
                tokenResponse.token
            }
        } catch (e: IOException) {
            null
        }
    }
}
