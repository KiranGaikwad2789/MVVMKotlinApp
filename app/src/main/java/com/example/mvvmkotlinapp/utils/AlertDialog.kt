package com.example.mvvmkotlinapp.utils

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.icu.text.CaseMap
import android.os.Build
import android.os.Message
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.mvvmkotlinapp.R

class AlertDialog {

    //http://developine.com/android-common-utility-functions-using-kotlin-code/

    companion object {

        @RequiresApi(Build.VERSION_CODES.M)
        fun basicAlert(context: Context, title:String, message:String, alertType:String) {

            val builder = AlertDialog.Builder(context)

            //set title for alert dialog
            builder.setTitle(title)
            //set message for alert dialog
            builder.setMessage(message)
            //builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes")
            { dialogInterface, which ->

                dialogInterface.dismiss()

                //Toast.makeText(context, "clicked yes", Toast.LENGTH_LONG).show()
            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                Toast.makeText(context, "clicked No", Toast.LENGTH_LONG).show()
            }


            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
             //alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.colorPrimary3))
             //alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.colorPrimary3))

            alertDialog.setCancelable(false)
            alertDialog.show()
        }

    }
}
