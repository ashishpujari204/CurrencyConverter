package com.ashish.currencyconverter.util

import android.app.Activity
import com.ashish.currencyconverter.storage.SharedPreferencesStorage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Constants {

    companion object {
        var font_name = "Fonts/google_sans_regular.ttf"
        var font_name_medium = "Fonts/google_sans_regular.ttf"
        var bold_font_name = "Fonts/google_sans_regular.ttf"
        var  KEYSTORE_HELPER_ALIES = "smart";
        private var PRIVATE_MODE = 0
        val PREFERANCE_NAME = "smartProcessor"
        private val TOKEN = "token"
        private val NAME = "name"
        private val USER_ID = "userId"
        private val USERNAME = "username"
        private val SITE_ID = "site_id"
        private val SITE_NAME = "site_name"
        private val IS_LOGIN = "is_login"
        private val ROLE = "role"
        private val APP_URL = "app_url"
        var DEFAULT_VALUE="NA"

        /**
         * Json key
         */
        val SUCCESS="success"
        val RESULT="result"
        val CONVERSATION_RATES="conversion_rates"

        /*fun saveUser(activity: Activity,userObject: User) {
            var storageRef=SharedPreferencesStorage(activity)
            storageRef.setString(USERNAME, userObject.mobile)
            storageRef.setString(NAME, userObject.name)
            storageRef.setString(TOKEN, userObject.authToken)
            storageRef.setString(USER_ID, userObject.userId)
            storageRef.setString(SITE_ID, userObject.mSiteId)
            storageRef.setString(SITE_NAME, userObject.mSiteName)
            storageRef.setString(ROLE, userObject.role.toString())
            storageRef.setString(IS_LOGIN, "1")
            storageRef.setString(APP_URL, userObject.app_url)
            Log.e("ENCRYPT",KeyStoreHelperForStorage.encrypt(KEYSTORE_HELPER_ALIES,userObject.name))
            Log.e("DECRYPT",KeyStoreHelperForStorage.decrypt(KEYSTORE_HELPER_ALIES,KeyStoreHelperForStorage.encrypt(KEYSTORE_HELPER_ALIES,userObject.name)))
        }*/
        fun getSiteId(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(SITE_ID)
        }
        fun getAppUrl(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(APP_URL)
        }
        fun getSiteName(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(SITE_NAME)
        }
        fun getUserName(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(USERNAME)
        }
        fun getRole(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(ROLE)
        }
        fun getName(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(NAME)
        }
        fun getIsLogin(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(IS_LOGIN)
        }

        fun getUserId(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(USER_ID)
        }

        fun getToken(activity: Activity) : String{
            return SharedPreferencesStorage(activity).getString(TOKEN)
        }
        fun convertDate(date: String): String? {
            return try {
                var format =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val newDate = format.parse(date)
                format =
                    SimpleDateFormat("dd MMM, yyyy hh:mm", Locale.ENGLISH)
                format.format(newDate)
            } catch (e: ParseException) {
                "NA"
            }
        }
    }

}