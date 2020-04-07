package com.ashish.currencyconverter.baseclasses

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ashish.currencyconverter.BuildConfig

abstract class BaseFragment : Fragment() {

    private var parentActivity: BaseActivity? = null
    private lateinit var mProgressDialog: ProgressDialog
    private var LOG="BaseFragment"
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.parentActivity = activity
            //activity?.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        mProgressDialog = ProgressDialog(getBaseActivity())
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.isIndeterminate = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    fun getBaseActivity() = parentActivity



    interface CallBack {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
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
        Toast.makeText(activity,message, Toast.LENGTH_LONG).show()
    }
    fun showAlertDialog(dialogBuilder: AlertDialog.Builder.() -> Unit) {
        val builder = AlertDialog.Builder(getBaseActivity())
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


    fun logError(message : String) {
        if(BuildConfig.DEBUG)
            Log.e(LOG,message)
    }
    fun logDebug(message : String) {
        if(BuildConfig.DEBUG)
            Log.d(LOG,message)
    }
    fun logInfo(message : String) {
        if(BuildConfig.DEBUG)
            Log.i(LOG,message)
    }
}