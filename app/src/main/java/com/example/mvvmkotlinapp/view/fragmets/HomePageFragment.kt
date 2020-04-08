package com.example.mvvmkotlinapp.view.fragmets

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mvvmkotlinapp.BuildConfig
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentHomePageBinding
import com.example.mvvmkotlinapp.receiver.AlarmReceive
import com.example.mvvmkotlinapp.viewmodel.HomePageViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class HomePageFragment : Fragment() {

    lateinit var homePageViewModel:HomePageViewModel
    lateinit var homePageBinding : FragmentHomePageBinding

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    protected var mLastLocation: Location? = null

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    var pendingIntent : PendingIntent? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homePageBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_home_page, container, false)
        val view: View = homePageBinding.getRoot()

        //initalizeCurrentLocationInstance()

        homePageBinding.switchStartDuty.setOnCheckedChangeListener({ _ , isChecked ->
            if (isChecked){
                if (alarmMgr == null) {
                    val manager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmIntent = Intent(activity, AlarmReceive::class.java)
                    pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, 0)
                    val interval = 10000
                    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval.toLong(), pendingIntent
                    )
                }
            }else{
                alarmMgr!!.cancel(pendingIntent)
            }
        })
        return view
    }

    private fun initalizeCurrentLocationInstance() {
        mFusedLocationClient = this!!.activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }
    }


    public override fun onStart() {
        super.onStart()
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        this!!.activity?.let {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLastLocation = task.result

                        Log.d("fragment latLng ", "--->>>>"+ mLastLocation!!.latitude)

                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.exception)
                        //showMessage(getString(R.string.no_location_detected))
                    }
                }
        }
    }

    /**
     * Shows a [] using `text`.

     * @param text The Snackbar text.
     */
    private fun showMessage(text: String) {

    }

    /**
     * Shows a [].

     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * *
     * @param actionStringId   The text of the action item.
     * *
     * @param listener         The listener associated with the Snackbar action.
     */
    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
                             listener: View.OnClickListener) {

        Toast.makeText(activity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = activity?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun requestPermissions() {
        val shouldProvideRationale = activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale!!) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")


        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.app_name, R.string.app_name,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })
            }
        }
    }

    companion object {

        private val TAG = "LocationProvider"

        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }


}