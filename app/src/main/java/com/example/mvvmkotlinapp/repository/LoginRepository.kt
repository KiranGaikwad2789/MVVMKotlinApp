package com.example.mvvmkotlinapp.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.APIInterface
import com.example.mvvmkotlinapp.model.LoginInfo
import com.example.mvvmkotlinapp.model.RegisterUserModel
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.User
import com.example.mvvmkotlinapp.repository.room.UserDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class LoginRepository {

    var appDatabase: AppDatabase? =null

    companion object{
        var mInstance: LoginRepository? = null
        public fun getmInstance(): LoginRepository? {
            if (mInstance == null) {
                mInstance = LoginRepository()
            }
            return mInstance
        }
    }

    fun LoginRepository(application: Application?) {
        appDatabase = AppDatabase.getDatabase(application!!)
    }

    fun loginRequest(username: String, password: String): MutableLiveData<LoginInfo> {

        val loginData: MutableLiveData<LoginInfo> = MutableLiveData()

        val apiInterface: APIInterface = RetrofitInstance.getClient()!!.create(APIInterface::class.java)

        val call: Call<LoginInfo>? = apiInterface.doLogin(username, password)

        call!!.enqueue(object : Callback<LoginInfo> {

            override fun onResponse(call: Call<LoginInfo>, response: Response<LoginInfo>) {
                loginData.setValue(response.body())
            }

            override fun onFailure(call: Call<LoginInfo>, t: Throwable) {
                loginData.setValue(null)
            }
        })

        return loginData
    }


    private class InsertUserAsyncTask(userDao: UserDao) : AsyncTask<User, Unit, Unit>() {
        lateinit var userDao: UserDao
        override fun doInBackground(vararg p0: User?) {
            userDao.insertAll(p0[0]!!)
        }
    }

    fun insert(user: User?) {
        lateinit var userDao: UserDao
        InsertUserAsyncTask(userDao).execute(user)
    }

}