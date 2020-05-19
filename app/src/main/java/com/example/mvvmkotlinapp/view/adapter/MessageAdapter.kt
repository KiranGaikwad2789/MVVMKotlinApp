package com.example.mvvmkotlinapp.view.adapter

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.model.ChatMessage
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_message_item.view.*
import java.io.File


class MessageAdapter (val messages: ArrayList<ChatMessage>,var context: Context) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var userSession: UserSession? =null
    private var currentDate: DateTime? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        userSession=UserSession(context)
        currentDate= DateTime()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_message_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(messages[position],userSession,currentDate,context)
    }

    override fun getItemCount() = messages.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindForecast(
            message: ChatMessage,
            userSession: UserSession?,
            currentDate: DateTime?,
            context: Context
        ) {

            if (message.messageType.equals("TEXT")) {
                if (message.senderId.equals(userSession?.getIMEI())) {
                    itemView.layoutSenderID.visibility=View.VISIBLE
                    itemView.txtMessage.text = message.message
                    itemView.txtSenderMessageTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                } else {
                    itemView.layoutReceiverID.visibility=View.VISIBLE
                    itemView.txtMessageReciver.text = message.message
                    itemView.txtMessageReciverTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                }
            } else  if (message.messageType.equals("IMAGE")) {
                if (message.senderId.equals(userSession?.getIMEI())) {
                    itemView.imgSenderImage.visibility=View.VISIBLE
                    Picasso.with(context).load(message.message).into(itemView.imgSenderImage)
                } else {
                    itemView.imgReceiverImage.visibility=View.VISIBLE
                    Picasso.with(context).load(message.message)
                        .into(itemView.imgReceiverImage)
                }
            }else if (message.messageType.equals("PDF")) {
                Log.e("Doc URL: ",""+message.message)
                if (message.senderId.equals(userSession?.getIMEI())) {
                    itemView.layoutSenderDocs.visibility=View.VISIBLE
                    itemView.txtSendDocsFileName.text = message.fileName
                    var fileExtOnly: String? = message.fileName?.substring(message.fileName!!.lastIndexOf(".")+1)
                    itemView.txtSendDocsFileType.text = fileExtOnly
                    itemView.txtSendDocsFileTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                } else {
                    itemView.layoutReceiverDocs.visibility=View.VISIBLE
                    itemView.txtReceiverDocsFileName.text = message.fileName
                    var fileExtOnly: String? = message.fileName?.substring(message.fileName!!.lastIndexOf(".")+1)
                    itemView.txtReceiverDocsFileType.text = fileExtOnly
                    itemView.txtReceiverDocsFileTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                }

                itemView.txtSendDocsFileName.setOnClickListener {
                    Toast.makeText(context,""+message.fileName,Toast.LENGTH_SHORT).show()
                    var file:File =  File(Environment.getExternalStorageDirectory(), "firebasestorage/"+message.fileName);

                    if(file.exists()){
                        Log.e("File exists","Exists")

                        val myMime = MimeTypeMap.getSingleton()
                        val newIntent = Intent(Intent.ACTION_VIEW)
                        var fileExtOnly: String? = message.fileName?.substring(message.fileName!!.lastIndexOf(".")+1)
                        val mimeType = myMime.getMimeTypeFromExtension(fileExtOnly)
                        newIntent.setDataAndType(Uri.fromFile(File(message.fileName)), mimeType)
                        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        try {
                            context.startActivity(newIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG)
                                .show()
                        }

                    } else{
                        Log.e("File not exists","Not Exists")
                        downloadFile(message.message,message.fileName,context)
                    }
                }
            }
        }

        public fun downloadFile(url:String?,fileName:String?,context:Context) {


            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReferenceFromUrl(url.toString())

            val pd = ProgressDialog(context)
            pd.setTitle(""+fileName)
            pd.setMessage("Downloading Please Wait!")
            pd.isIndeterminate = true
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            pd.show()

            val rootPath = File(Environment.getExternalStorageDirectory(), "firebasestorage")
            if (!rootPath.exists()) {
                rootPath.mkdirs()
            }

            val localFile = File(rootPath, fileName)
            storageRef.getFile(localFile).addOnSuccessListener(object :
                OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                override fun onSuccess(taskSnapshot: FileDownloadTask.TaskSnapshot?) {
                    Log.e(
                        "firebase ",
                        ";local tem file created  created $localFile"
                    )
                    if (localFile.canRead()) {
                        pd.dismiss()
                    }
                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(@NonNull exception: Exception) {
                    Log.e(
                        "firebase ",
                        ";local tem file not created  created $exception"
                    )
                    Toast.makeText(context, "Download Incompleted", Toast.LENGTH_LONG).show()
                }
            })






             //https://firebasestorage.googleapis.com/v0/b/chatapp-72cf4.appspot.com/o/images%2Fc31010d5-d4a2-49ea-b130-36e1947a34bb?alt=media&token=548c515f-e5c7-464b-a3b0-a3c8d0919740


            /*val storage = FirebaseStorage.getInstance()
            val storageRef = storage!!.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/chatapp-72cf4.appspot.com/o/images%2Fc31010d5-d4a2-49ea-b130-36e1947a34bb?alt=media&token=548c515f-e5c7-464b-a3b0-a3c8d0919740")
            val islandRef = storageRef.child("images")
            val rootPath = File(Environment.getExternalStorageDirectory(), "firebasestorage")
            if (!rootPath.exists()) {
                rootPath.mkdirs()
            }
            val localFile = File(rootPath, fileName)
            islandRef.getFile(localFile)
                .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                    Log.e(
                        "firebase ",
                        ";local tem file created  created " + localFile.toString()
                    )
                    //  updateDb(timestamp,localFile.toString(),position);
                }).addOnFailureListener(OnFailureListener { exception ->
                    Log.e(
                        "firebase ",
                        ";local tem file not created  created $exception"
                    )
                })*/
        }


    }



}