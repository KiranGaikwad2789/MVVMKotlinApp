package com.example.mvvmkotlinapp.view.adapter

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.model.ChatImageModel
import com.example.mvvmkotlinapp.model.ChatMessage
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.fragmets.ChatImageFrgament
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_message_item.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class MessageAdapter (val messages: ArrayList<ChatMessage>,var context: Context) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var userSession: UserSession? =null
    private var currentDate: DateTime? =null
    private val mHandler = Handler()


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
                    itemView.cardSenderImage.visibility=View.VISIBLE
                    Picasso.with(context).load(message.message).into(itemView.imgSenderImage)
                    itemView.txtSendImageImagetime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                } else {
                    itemView.cardReceiverImage.visibility=View.VISIBLE
                    Picasso.with(context).load(message.message).into(itemView.imgReceiverImage)
                    itemView.txtReceiverImageTime.text = currentDate?.chatDateForamt(message.timeStamp!!).toString()
                }

                itemView.cardSenderImage.setOnClickListener {
                    var chaImageModel= ChatImageModel(message.message?.toUri(),"IMAGESHOW","fileName")
                    (context as HomePageActivity).commonMethodForFragment(ChatImageFrgament(chaImageModel),true)
                }
                itemView.cardReceiverImage.setOnClickListener {
                    var chaImageModel= ChatImageModel(message.message?.toUri(),"IMAGESHOW","fileName")
                    (context as HomePageActivity).commonMethodForFragment(ChatImageFrgament(chaImageModel),true)
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
                    var file:File =  File(Environment.getExternalStorageDirectory(), "firebasestorage/"+message.fileName);

                    if(file.exists()){
                        val myMime = MimeTypeMap.getSingleton()
                        val newIntent = Intent(Intent.ACTION_VIEW)
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        var fileExtOnly: String? = message.fileName?.substring(message.fileName!!.lastIndexOf(".")+1)
                        val mimeType = myMime.getMimeTypeFromExtension(fileExtOnly)

                        var apkURI:Uri = FileProvider.getUriForFile(
                             context, context.getApplicationContext().getPackageName() + ".provider", file);
                        newIntent.setDataAndType(apkURI, mimeType);
                        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            context.startActivity(newIntent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else{
                        itemView.progressBar.visibility=View.VISIBLE
                        downloadFile(message.message,message.fileName,context)
                    }
                }
            }
        }

        public fun downloadFile(url:String?,fileName:String?,context:Context) {

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReferenceFromUrl(url.toString())
            var handler: Handler? = null
            var progressStatus = 0
            var isStarted = false

            handler = Handler(Handler.Callback {
                if (isStarted) {
                    progressStatus++
                }
                itemView.progressBar.progress = progressStatus
                handler?.sendEmptyMessageDelayed(0, 100)

                true
            })


            Thread(Runnable {
                while (progressStatus < 100) {
                    progressStatus += 1
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    handler?.post {
                        itemView.progressBar.progress = progressStatus
                    }
                }
            }).start()

            val rootPath = File(Environment.getExternalStorageDirectory(), "firebasestorage")
            if (!rootPath.exists()) {
                rootPath.mkdirs()
            }

            val localFile = File(rootPath, fileName)
            storageRef.getFile(localFile).addOnSuccessListener(object :
                OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                override fun onSuccess(taskSnapshot: FileDownloadTask.TaskSnapshot?) {
                    Log.e("firebase ", ";local tem file created  created $localFile")
                    handler?.post {
                        itemView.progressBar.progress = 100
                    }
                    handler?.removeCallbacks(null)
                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show()
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(@NonNull exception: Exception) {
                    Log.e("firebase ", ";local tem file not created  created $exception")
                    handler?.removeCallbacks(null)
                    Toast.makeText(context, "Download Incompleted", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}