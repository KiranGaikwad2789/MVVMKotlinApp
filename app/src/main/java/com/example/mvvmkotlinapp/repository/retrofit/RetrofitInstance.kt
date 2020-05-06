package com.example.mvvmkotlinapp.repository.retrofit

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {

    companion object{

        fun getClient(): Retrofit? {

            /*val gson = GsonBuilder()
                .setLenient()
                .create()
            return Retrofit.Builder()
                .baseUrl("https://chatapp-72cf4.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()*/

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            //        OkHttpClient okHttpClient =new OkHttpClient().newBuilder().build();
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val originalRequest = chain.request()
                    val response = chain.proceed(originalRequest)

                    // todo deal with the issues the way you need to
                    if (response.code() == 401) {
                        //AppController.getmInstance().accessUnAuthorization()
                        return@Interceptor response
                    }
                    response
                })
                .addInterceptor(interceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://chatapp-72cf4.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

        }
    }

}