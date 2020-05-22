package com.example.mvvmkotlinapp.view.fragmets

import android.app.ProgressDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentChatImageFrgamentBinding
import com.example.mvvmkotlinapp.model.ChatImageModel
import com.example.mvvmkotlinapp.utils.CustomProgressDialog
import com.example.mvvmkotlinapp.view.AESUtils
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import com.firebase.client.Firebase
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatImageFrgament(var chaImageModel: ChatImageModel) : Fragment() {

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: FragmentChatImageFrgamentBinding

    var storageReference: StorageReference? = null
    var storage: FirebaseStorage? = null
    private var userSession: UserSession? =null
    private var currentDate: DateTime? =null
    private var reference1: Firebase? = null
    private var reference2: Firebase? = null
    private val skyggeProgressDialog = CustomProgressDialog()
    private var aesEncryptions: AESUtils? =null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_chat_image_frgament, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        Picasso.with(context).load(chaImageModel.filePathData).into(binding.imgSelectedImagetoUpload)

        userSession=UserSession(activity)
        currentDate= DateTime()
        aesEncryptions= AESUtils()


        reference1 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession?.getIMEI().toString() + "_" + userSession!!.getChatWith())
        reference2 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession!!.getChatWith().toString() + "_" + userSession?.getIMEI())

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReferenceFromUrl("gs://chatapp-72cf4.appspot.com")


        if(chaImageModel.type.equals("IMAGESHOW")){
            binding.mainActivityTextContainer.visibility=View.GONE
        }else if(chaImageModel.type.equals("IMAGE")){
            binding.mainActivityTextContainer.visibility=View.VISIBLE
        }

        binding.imgBtnSendMessage.setOnClickListener {
            uploadImage(chaImageModel.filePathData, chaImageModel.type.toString(),
                chaImageModel.fileName.toString()
            )
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage(filePathData: Uri?, type:String, fileName:String) {

        if(filePathData != null){
            activity?.let { skyggeProgressDialog.show(it,"Please Wait...") }

            val ref = storageReference?.child("images/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePathData!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    val map: MutableMap<String, String> = HashMap()

                    map["chatId"] = ""+aesEncryptions?.encrypt(userSession?.getMobile().toString()) + "_" + aesEncryptions?.encrypt(userSession?.getChatWith().toString())
                    map["messageId"] = ""+aesEncryptions?.encrypt(userSession?.getUserId().toString()) + "" + aesEncryptions?.encrypt(currentDate!!.orderDateFormater())
                    map["senderId"] = ""+aesEncryptions?.encrypt(userSession?.getIMEI().toString())
                    map["receiverId"] = ""+aesEncryptions?.encrypt(userSession?.getChatWith().toString())
                    map["message"] = ""+aesEncryptions?.encrypt(downloadUri.toString())
                    map["fileName"] = ""+aesEncryptions?.encrypt(fileName)
                    map["timeStamp"] = ""+aesEncryptions?.encrypt(currentDate!!.getDateTime())
                    map["messageType"] = ""+aesEncryptions?.encrypt(type)
                    reference1!!.push().setValue(map)
                    reference2!!.push().setValue(map)
                    skyggeProgressDialog?.dialog?.dismiss()
                    activity?.onBackPressed()
                } else {
                    skyggeProgressDialog?.dialog?.dismiss()
                    activity?.onBackPressed()
                    // Handle failures
                }
            }?.addOnFailureListener{
                skyggeProgressDialog?.dialog?.dismiss()
                activity?.onBackPressed()
            }
        }else{
            Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
}
