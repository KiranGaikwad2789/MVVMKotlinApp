package com.example.mvvmkotlinapp.view.fragmets

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentChatBinding
import com.example.mvvmkotlinapp.interfaces.DrawerLocker
import com.example.mvvmkotlinapp.model.ChatImageModel
import com.example.mvvmkotlinapp.model.ChatMessage
import com.example.mvvmkotlinapp.utils.CustomProgressDialog
import com.example.mvvmkotlinapp.utils.FileCompressor
import com.example.mvvmkotlinapp.utils.ImagePicker
import com.example.mvvmkotlinapp.view.AESUtils
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.MessageAdapter
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.dialog_chat_options.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: FragmentChatBinding

    private var reference1: Firebase? = null
    private var reference2: Firebase? = null
    private var currentDate: DateTime? =null
    private var userSession: UserSession? =null
    private val arryListMessageList: ArrayList<ChatMessage> = ArrayList()
    private var adapter: MessageAdapter? = null
    private val skyggeProgressDialog = CustomProgressDialog()
    private val PICK_IMAGE_REQUEST = 71
    private val REQUEST_IMAGE_CAPTURE = 1
    val PDF : Int = 0
    private var imagePicker: ImagePicker? =null
    private var mCompressor: FileCompressor? =null;
    private var mPhotoFile: File? =null
    private var aesEncryptions: AESUtils? =null

    private var filePath: Uri? = null

    //Firebase
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_chat, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        getObjectsInitialize(view)

        setMessageAdapter()

        var msg = "123456";
        var keyStr = "abcdef"
        var ivStr = "ABCDEF"


        var ansBase64 = aesEncryptions?.encryptStrAndToBase64(ivStr, keyStr, msg);
        System.out.println("After Encrypt & To Base64: " + ansBase64);

        var deansBase64 = ansBase64?.let { aesEncryptions?.decryptStrAndFromBase64(ivStr, keyStr, it) };
        System.out.println("After Decrypt & From Base64: " + deansBase64)

        //Log.e("Encrypt ",""+ (aesEncryptions?.encrypt("Kiran")))
        //Log.e("Decrypt ",""+ (aesEncryptions?.decrypt(aesEncryptions?.encrypt("Kiran").toString())))


        binding.imgBtnSendMessage.setOnClickListener(View.OnClickListener {

            val messageText: String = binding.edtMessage.getText().toString()
            if (messageText != "") {

                val map: MutableMap<String, String> = HashMap()

                map["chatId"] = ""+aesEncryptions?.encrypt(userSession?.getMobile().toString()) + "_" + aesEncryptions?.encrypt(userSession?.getChatWith().toString())
                map["messageId"] = ""+aesEncryptions?.encrypt(userSession?.getUserId().toString()) + "" + aesEncryptions?.encrypt(currentDate!!.orderDateFormater())
                map["senderId"] = ""+aesEncryptions?.encrypt(userSession?.getIMEI().toString())
                map["receiverId"] = ""+aesEncryptions?.encrypt(userSession?.getChatWith().toString())
                map["message"] = ""+aesEncryptions?.encrypt(messageText)
                map["fileName"] = ""+aesEncryptions?.encrypt(messageText)
                map["timeStamp"] = ""+aesEncryptions?.encrypt(currentDate!!.getDateTime())
                map["messageType"] = ""+aesEncryptions?.encrypt("TEXT")

                reference1!!.push().setValue(map)
                reference2!!.push().setValue(map)
                binding.edtMessage.setText("")
            }
        })

        binding.imgBtnAttachment.setOnClickListener(View.OnClickListener {
            showChatOptionsDialog()
        })

        activity?.let { skyggeProgressDialog.show(it,"Please Wait...") }
        reference1?.addChildEventListener(object : EventListener, ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                val map = p0?.getValue(Map::class.java)

                val chatId = aesEncryptions?.decrypt(map?.get("chatId").toString())
                val messageId = aesEncryptions?.decrypt(map?.get("messageId").toString())
                val senderId = aesEncryptions?.decrypt(map?.get("senderId").toString())
                val receiverId = aesEncryptions?.decrypt(map?.get("receiverId").toString())
                val message = aesEncryptions?.decrypt(map?.get("message").toString())
                val fileName = aesEncryptions?.decrypt(map?.get("fileName").toString())
                val timeStamp = aesEncryptions?.decrypt(map?.get("timeStamp").toString())
                val messageType = aesEncryptions?.decrypt(map?.get("messageType").toString())

                var chatMessage=ChatMessage(chatId,messageId,senderId,receiverId,message,timeStamp,messageType,fileName)
                arryListMessageList.add(chatMessage)
                adapter?.notifyDataSetChanged()
                skyggeProgressDialog?.dialog?.dismiss()
            }

            override fun onCancelled(p0: FirebaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot?) {}
        })
        return view
    }

    private fun setMessageAdapter() {
        arryListMessageList.clear()
        binding.recyclerViewMessageList.adapter = null
        adapter = activity?.let { MessageAdapter(arryListMessageList, it) }
        binding.recyclerViewMessageList.adapter = adapter
        binding.recyclerViewMessageList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun getObjectsInitialize(view: View) {
        currentDate= DateTime()
        imagePicker= ImagePicker()
        mCompressor= FileCompressor()
        userSession=UserSession(activity)
        Firebase.setAndroidContext(activity)
        aesEncryptions= AESUtils()

        (activity as AppCompatActivity)?.supportActionBar?.hide()
        (activity as DrawerLocker).setDrawerEnabled(false)
        (getActivity() as HomePageActivity)?.visibleMenuItems(14)

        val toolbar: Toolbar = view?.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.setTitle(""+userSession!!.getChatWith().toString())

        reference1 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession?.getIMEI().toString() + "_" + userSession!!.getChatWith())
        reference2 = Firebase("https://chatapp-72cf4.firebaseio.com/messages/" + userSession!!.getChatWith().toString() + "_" + userSession?.getIMEI())

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReferenceFromUrl("gs://chatapp-72cf4.appspot.com")
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            filePath = data.data
            var bitmap = MediaStore.Images.Media.getBitmap(activity?.getContentResolver(), filePath)
            var nh = ( bitmap.getHeight() * (512.0 / bitmap.getWidth())).toInt()
            var resizedBitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, false);
            var tempUri: Uri? = activity?.let { getImageUri(it, resizedBitmap) }
            Log.e("FilePath Gallery ",""+tempUri)

            var chaImageModel=ChatImageModel(tempUri,"IMAGE","fileName")
            (activity as HomePageActivity).commonMethodForFragment(ChatImageFrgament(chaImageModel),true)

        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            var bitmap = data?.extras?.get("data") as Bitmap
            var nh = ( bitmap.getHeight() * (512.0 / bitmap.getWidth())).toInt()
            var resizedBitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, false);
            var tempUri: Uri? = activity?.let { getImageUri(it, resizedBitmap) }
            Log.e("FilePath Camera ",""+tempUri)

            var chaImageModel=ChatImageModel(tempUri,"IMAGE","fileName")
            (activity as HomePageActivity).commonMethodForFragment(ChatImageFrgament(chaImageModel),true)

        }else if (requestCode == PDF && resultCode == RESULT_OK && data != null && data.data != null) {

            filePath = data!!.data
            var filePathStr:String= filePath.toString()
            var fileNameOnly:String =filePathStr.substring(filePathStr.lastIndexOf("/")+1);
            Log.e("FilePath path: ", filePath.toString())
            Log.e("FilePath fileNameOnly: ", fileNameOnly)
            try {
                filePath?.let { uploadImage(it,"PDF",fileNameOnly) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getImageUri(inContext: Context, inImage:Bitmap?):Uri {
           // var bytes = ByteArrayOutputStream();
            //inImage?.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            var path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
    }

    private fun getRealPathFromURI(uri:Uri ): String {
    var path = ""
    if (activity?.getContentResolver()!= null) {
        var cursor:Cursor?= activity?.getContentResolver()!!.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            var idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }
    }
        return path;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage(filePathData:Uri,type:String,fileName:String) {

        if(filePathData != null){
            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

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
                    progressDialog.dismiss()
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showChatOptionsDialog() {

        val dialog = context?.let { BottomSheetDialog(it, R.style.Theme_Dialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.dialog_chat_options)

        var imgCamera= dialog?.findViewById<ImageView>(R.id.imgCamera)
        var imgGallery= dialog?.findViewById<ImageView>(R.id.imgGallery)
        var imgDocuments= dialog?.findViewById<ImageView>(R.id.imgDocuments)
        var imgLocation= dialog?.findViewById<ImageView>(R.id.imgLocation)

        imgCamera?.setOnClickListener {
            dialog?.dismiss()
            dispatchTakePictureIntent()
        }
        imgGallery?.setOnClickListener {
            dialog?.dismiss()
            chooseImage()
        }
        imgDocuments?.setOnClickListener {
            dialog?.dismiss()
            uploadDocuments()
        }
        imgLocation?.setOnClickListener {
        }
        dialog?.show()
    }

    private fun uploadDocuments() {
        val intent = Intent()
        intent.type="application/*"
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (activity?.getPackageManager()?.let { takePictureIntent.resolveActivity(it) } != null) {
            startActivityForResult(
                takePictureIntent, REQUEST_IMAGE_CAPTURE
            )
        }
    }

    /*private fun dispatchTakePictureIntent() {

    var takePictureIntent:Intent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (activity?.getPackageManager()?.let { takePictureIntent.resolveActivity(it) } != null) {
            var photoFile: File? = null;
            try {
                photoFile = createImageFile();
            } catch (ex:IOException ) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                var photoURI = FileProvider.getUriForFile(
                    activity!!,
                BuildConfig.APPLICATION_ID + ".provider",
                photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }*/


    private fun createImageFile():File{
    // Create an image file name
    var timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format( Date());
    var mFileName = "JPEG_" + timeStamp + "_";
    var storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    var  mFile :File= File.createTempFile(mFileName, ".jpg", storageDir);
    return mFile;
  }
}
