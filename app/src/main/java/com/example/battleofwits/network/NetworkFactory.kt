package com.example.battleofwits.network
import android.content.Context
import com.example.battleofwits.PREFERENCES
import com.example.battleofwits.URL_MAIN
import android.content.SharedPreferences
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.riversun.okhttp3.OkHttp3CookieHelper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkFactory {

    /**
     * Companion object to create the GithubApiService
     */
    companion object  {

        private var url: String = URL_MAIN
        private val cookieHelper = OkHttp3CookieHelper()

        fun getData(context: Context): ServerApiService {

            val mSettings: SharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val token: String = mSettings.getString("device_token", "").toString()

            cookieHelper.setCookie(url, "session", token)

            Log.d("***Token***", token)

            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();

            return retrofit.create(ServerApiService::class.java);
        }

    }

}