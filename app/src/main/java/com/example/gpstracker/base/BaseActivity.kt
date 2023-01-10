package com.example.todo.base
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity:AppCompatActivity()
{
    fun makeToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
    fun makeToast(messageId:Int){
        Toast.makeText(this,messageId,Toast.LENGTH_LONG).show()
    }
    fun showDialoge(
        title:String?=null,
        message: String,
        posActionName:String?=null,
        posAction:DialogInterface.OnClickListener?=null,
        negActionName:String?=null,
        negAction:DialogInterface.OnClickListener?=null,
        deleteAction: DialogInterface.OnCancelListener?=null,
                    ) {
        val builder=AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton(posActionName,posAction)
        builder.setNegativeButton(negActionName,negAction)
        builder.setOnCancelListener(deleteAction)
        builder.show()
    }
    fun showDialoge(titleId:Int?=null,
                    messageId: Int,
                    posActionName:Int?=null,
                    posAction:DialogInterface.OnClickListener?=null,
                    negActionName:Int?=null,
                    negAction:DialogInterface.OnClickListener?=null){
        val builder=AlertDialog.Builder(this)
        builder.setMessage(messageId)
        if (titleId!=null)
            builder.setTitle(titleId)
        if (posActionName!=null)
            builder.setPositiveButton(posActionName,posAction)
        if (negActionName!=null)
            builder.setNegativeButton(negActionName,negAction)
        builder.show()
    }
//    fun makeSnackbar(messageID: Int,
//                     actionString:Int?=null,
//                     click:View.OnClickListener?=null){
//        val snackbar=Snackbar.make(root,messageID,Snackbar.LENGTH_LONG)
//        if (actionString!=null){
//            snackbar.setAction(actionString,click)
//        }
//        snackbar.show()}
//
//    fun makeSnackbar(message: String,
//                     actionString:String?=null,
//                     click:View.OnClickListener?=null){
//        val snackbar=Snackbar.make(root,message,Snackbar.LENGTH_LONG)
//        if (actionString!=null){
//            snackbar.setAction(actionString,click)
//        }
//        snackbar.show()}


}