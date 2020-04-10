package com.example.mvvmkotlinapp.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.mvvmkotlinapp.interfaces.APIInterface
import com.example.mvvmkotlinapp.repository.retrofit.RetrofitInstance
import com.example.mvvmkotlinapp.repository.room.AppDatabase
import com.example.mvvmkotlinapp.repository.room.CurrentLocation
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrentLocationRepository {

    private var myDataBase: AppDatabase? = null

    private val TAG = "EmployeeRepository"
    private var employees: ArrayList<CurrentLocation> = ArrayList()
    private val mutableLiveData: MutableLiveData<List<CurrentLocation>> = MutableLiveData<List<CurrentLocation>>()


    companion object{
        var mInstance: CurrentLocationRepository? = null
        public fun getmInstance(): CurrentLocationRepository? {
            if (mInstance == null) {
                mInstance = CurrentLocationRepository()
            }
            return mInstance
        }
    }

    fun insertLocation(context: Context?, location: CurrentLocation?) {
        myDataBase= AppDatabase.getDatabase(context!!)
        doAsync {
            myDataBase!!.locationDao().insertAll(location!!)
        }
    }

    fun getAllLocation(context: Context?):List<CurrentLocation>{
        return AppDatabase.getDatabase(context!!).locationDao().getAllLocation()
    }

    /*fun getMutableLiveData(): MutableLiveData<List<CurrentLocation?>?>? {
        val apiInterface: APIInterface = RetrofitInstance.getClient()!!.create(APIInterface::class.java)

        val call: Call<CurrentLocation> = apiInterface.getEmployees()

        call.enqueue(object : Callback<CurrentLocation?>() {

            fun onResponse(call: Call<CurrentLocation?>?, response: Response<CurrentLocation?>) {
                val employeeDBResponse: CurrentLocation = response.body()
                if (employeeDBResponse != null && employeeDBResponse.getEmployee() != null) {
                    employees = employeeDBResponse.getEmployee()
                    mutableLiveData.setValue(employees)
                }
            }

            fun onFailure(call: Call<CurrentLocation?>?, t: Throwable?) {

            }
        })
        return mutableLiveData
    }*/
}