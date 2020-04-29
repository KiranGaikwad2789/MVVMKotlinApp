package com.example.mvvmkotlinapp.repository.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{
        fun getClient(): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            //        OkHttpClient okHttpClient =new OkHttpClient().newBuilder().build();
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val originalRequest = chain.request()
                    //val builder = originalRequest.newBuilder().header("Token",AppController.getmInstance().getJwtToken())
                    //val newRequest = builder.build()
                    val response = chain.proceed(originalRequest)
                    /*Request request = chain.request();
                                    okhttp3.Response response = chain.proceed(request);*/
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
                .baseUrl("http://atzcart.com/members_new/webapis/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
    }

}