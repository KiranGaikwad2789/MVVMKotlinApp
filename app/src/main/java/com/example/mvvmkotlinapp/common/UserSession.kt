package com.example.mvvmkotlinapp.common

import android.content.Context
import android.content.SharedPreferences

class UserSession {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor? = null
    var _context: Context? = null
    val PRIVATE_MODE = 0

    val PREFER_NAME = "userdata_paretapplication"
    val IS_USER_LOGIN = "IsUserLoggedIn"
    val KEY_USERID = "userid"
    val KEY_USERNAME = "username"
    val KEY_EMAIL = "useremail"
    val KEY_MOBILE = "usermobile"
    val KEY_CITY = "city"
    val KEY_CHATWITH = "chatWith"

    private val KEY_JWT_TOKEN = "jwtToken"
    private val KEY_SELECTED_CATEGORY_LIST = "selectedCategoryList"
    private val KEY_SOURCING_CATEGORY_LIST = "SourcingCategoryList"


    val KEY_API = "key"

    constructor(_context: Context?) {
        this._context = _context
        pref = _context!!.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit()
    }

    fun setUserId(userId: String?) {
        editor!!.putString(KEY_USERID, userId)
        editor!!.commit()
    }

    fun getUserId(): String? {
        return pref!!.getString(KEY_USERID, null)
    }

    fun setUsername(username: String?) {
        editor!!.putString(KEY_USERNAME, username)
        editor!!.commit()
    }

    fun getUsername(): String? {
        return pref!!.getString(KEY_USERNAME, null)
    }

    fun setEmail(userId: String?) {
        editor!!.putString(KEY_EMAIL, userId)
        editor!!.commit()
    }

    fun getEmail(): String? {
        return pref!!.getString(KEY_EMAIL, null)
    }

    fun setMobile(mobile: String?) {
        editor!!.putString(KEY_MOBILE, mobile)
        editor!!.commit()
    }

    fun getMobile(): String? {
        return pref!!.getString(KEY_MOBILE, null)
    }

    fun setCurrentCity(mobile: String?) {
        editor!!.putString(KEY_CITY, mobile)
        editor!!.commit()
    }

    fun getCurrentCity(): String? {
        return pref!!.getString(KEY_CITY, null)
    }

    fun clearUserSession() {
        setUserId(null)
        setUsername(null)
        setMobile(null)
        setEmail(null)
    }


    fun setChatWith(username: String?) {
        editor!!.putString(KEY_CHATWITH, username)
        editor!!.commit()
    }

    fun getChatWith(): String? {
        return pref!!.getString(KEY_CHATWITH, null)
    }
}