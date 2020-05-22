package com.example.mvvmkotlinapp.model

import com.example.mvvmkotlinapp.view.AESUtils

private var aesEncryptions: AESUtils? =AESUtils()

class FirebaseUser(val user_id: String? =null, var username: String? =null, var mobilenumber: String? =null){

}