package com.ashish.currencyconverter.baseclasses

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ashish.currencyconverter.BuildConfig

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var mProgressDialog: ProgressDialog

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.isIndeterminate = true
    }
    fun showProgressDialog() {
        if(!mProgressDialog.isShowing) {
            mProgressDialog.show()
        }
    }

    fun stopProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }


    fun showToast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
    fun showAlertDialog(dialogBuilder: AlertDialog.Builder.() -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.dialogBuilder()
        val dialog = builder.create()
        dialog.show()
    }

    fun AlertDialog.Builder.positiveButton(text: String = "Okay", handleClick: (which: Int) -> Unit = {}) {
        this.setPositiveButton(text) { _, which-> handleClick(which) }
    }

    fun AlertDialog.Builder.negativeButton(text: String = "Cancel", handleClick: (which: Int) -> Unit = {}) {
        this.setNegativeButton(text) { _, which-> handleClick(which) }
    }
    fun View.closeKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    fun logError(message: String) {
        if(BuildConfig.DEBUG)
        Log.e(this.localClassName,message)
    }
    fun logDebug(message : String) {
        if(BuildConfig.DEBUG)
        Log.d(this.localClassName,message)
    }
    fun logInfo(message : String) {
        if(BuildConfig.DEBUG)
        Log.i(this.localClassName,message)
    }
}