package com.example.mvvmkotlinapp.utils

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed


open class UIUpdater {

    // Create a Handler that uses the Main Looper to run in
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    private var mStatusChecker: Runnable? = null
    private var UPDATE_INTERVAL : Long = 2000

    /**
     * Creates an UIUpdater object, that can be used to
     * perform UIUpdates on a specified time interval.
     *
     * @param uiUpdater A runnable containing the update routine.
     */
    fun UIUpdater(uiUpdater: Runnable) {
        mStatusChecker = object : Runnable {
            override fun run() { // Run the passed runnable
                uiUpdater.run()
                // Re-run it after the update interval
                mHandler.postDelayed(this, UPDATE_INTERVAL)
            }
        }
    }

    /**
     * The same as the default constructor, but specifying the
     * intended update interval.
     *
     * @param uiUpdater A runnable containing the update routine.
     * @param interval  The interval over which the routine
     * should run (milliseconds).
     */
    fun UIUpdater(uiUpdater: Runnable?, interval: Long) {
        UPDATE_INTERVAL = interval
        (uiUpdater)
    }

    /**
     * Starts the periodical update routine (mStatusChecker
     * adds the callback to the handler).
     */
    @Synchronized
    fun startUpdates() {
        mStatusChecker!!.run()
    }

    /**
     * Stops the periodical update routine from running,
     * by removing the callback.
     */
    @Synchronized
    fun stopUpdates() {
        mHandler.removeCallbacks(mStatusChecker)
    }
}