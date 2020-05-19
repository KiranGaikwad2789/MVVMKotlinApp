package com.example.mvvmkotlinapp.view.fragmets

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
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
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.BuildConfig
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentChatBinding
import com.example.mvvmkotlinapp.interfaces.DrawerLocker
import com.example.mvvmkotlinapp.model.ChatMessage
import com.example.mvvmkotlinapp.utils.CustomProgressDialog
import com.example.mvvmkotlinapp.utils.FileCompressor
import com.example.mvvmkotlinapp.utils.ImagePicker
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.MessageAdapter
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import com.firebase.client.ChildEventListener
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
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

        binding.imgBtnSendMessage.setOnClickListener(View.OnClickListener {

            val messageText: String = binding.edtMessage.getText().toString()
            if (messageText != "") {

                val map: MutableMap<String, String> = HashMap()
                map["chatId"] = userSession?.getMobile() + "_" + userSession!!.getChatWith()
                map["messageId"] = userSession?.getUserId() + "" + currentDate!!.orderDateFormater()
                map["senderId"] = userSession?.getIMEI().toString()
                map["receiverId"] = userSession!!.getChatWith().toString()
                map["message"] = messageText
                map["fileName"] = messageText
                map["timeStamp"] = currentDate!!.getDateTime()
                map["messageType"] = "TEXT"
                reference1!!.push().setValue(map)
                reference2!!.push().setValue(map)
                binding.edtMessage.setText("")
            }
        })

        binding.imgBtnAttachment.setOnClickListener(View.OnClickListener {

            showProductQuantityDialog()
        })

        activity?.let { skyggeProgressDialog.show(it,"Please Wait...") }
        reference1?.addChildEventListener(object : EventListener, ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {

                val map = p0?.getValue(Map::class.java)

                val chatId = map?.get("chatId").toString()
                val messageId = map?.get("messageId").toString()
                val senderId = map?.get("senderId").toString()
                val receiverId = map?.get("receiverId").toString()
                val message = map?.get("message").toString()
                val fileName = map?.get("fileName").toString()
                val timeStamp = map?.get("timeStamp").toString()
                val messageType = map?.get("messageType").toString()

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
            Log.e("FilePath Gallery ",""+filePath)
            try {
                var bmp = activity?.let { imagePicker?.getImageFromResult(it, resultCode, data) };//your compressed bitmap here
                var filePath: Uri? = activity?.let { getImageUri(it, bmp) };
                filePath?.let { uploadImage(it,"IMAGE","fileName") }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            var bitmap = data?.extras?.get("data") as Bitmap
            var tempUri: Uri? = activity?.let { getImageUri(it, bitmap) }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity?.getContentResolver(), tempUri)
                bitmap = rotateImageIfRequired(bitmap, tempUri)
                bitmap = getResizedBitmap(bitmap, 500)
            } catch (e:IOException) {
                e.printStackTrace();
            }
            Log.e("FilePath Camera ",""+tempUri)
            tempUri?.let { uploadImage(it,"IMAGE","fileName") }

        }else if (requestCode == PDF) {

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


     public fun getResizedBitmap(image:Bitmap, maxSize:Int):Bitmap {
        var width = image.getWidth();
        var height = image.getHeight();

        var bitmapRatio = width.toFloat() / height.toFloat();
        if (bitmapRatio > 0) {
            width = maxSize;
            height =  (width / bitmapRatio).toInt()
        } else {
            height = maxSize;
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private fun rotateImageIfRequired(img:Bitmap , selectedImage:Uri? ):Bitmap {

        var ei:ExifInterface = ExifInterface(selectedImage?.getPath());
        var orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        when (orientation) {
             ExifInterface.ORIENTATION_ROTATE_90->rotateImage(img, 90)

            ExifInterface.ORIENTATION_ROTATE_180->rotateImage(img, 180)

            ExifInterface.ORIENTATION_ROTATE_270->rotateImage(img, 270)
        }
        return img
    }

    private  fun rotateImage(img:Bitmap ,degree:Int):Bitmap {
        var matrix = Matrix()
        matrix.postRotate(degree.toFloat());
        var rotatedImg:Bitmap = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
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
                    map["chatId"] =  userSession?.getMobile() + "_" + userSession!!.getChatWith()
                    map["messageId"] = userSession?.getUserId() +""+ currentDate!!.orderDateFormater()
                    map["senderId"] = userSession?.getIMEI().toString()
                    map["receiverId"] = userSession!!.getChatWith().toString()
                    map["message"] = downloadUri.toString()
                    map["fileName"] = fileName           //FileName only
                    map["timeStamp"] = currentDate!!.getDateTime()
                    map["messageType"] = type
                    reference1!!.push().setValue(map)
                    reference2!!.push().setValue(map)
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show()
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProductQuantityDialog() {

        val dialog = context?.let { Dialog(it, R.style.Theme_Dialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        //dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_chat_options)

        val imgCamera = dialog?.findViewById(R.id.imgCamera) as ImageView
        val imgGallery = dialog?.findViewById(R.id.imgGallery) as ImageView
        val imgDocuments = dialog?.findViewById(R.id.imgDocuments) as ImageView
        val imgLocation = dialog?.findViewById(R.id.imgLocation) as ImageView


        imgCamera.setOnClickListener {
            dialog.dismiss()
            dispatchTakePictureIntent()
        }
        imgGallery.setOnClickListener {
            dialog.dismiss()
            chooseImage()
        }
        imgDocuments.setOnClickListener {
            dialog.dismiss()
            uploadDocuments()
        }
        imgLocation.setOnClickListener {
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
