package com.example.gpstracker

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.todo.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isLocationPermissionGranted()){
        }
        else{
            requestLocationPermissionFromUser()
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

                Toast.makeText(this,"can't find location because", Toast.LENGTH_LONG).show()
            }
        }
    private fun requestLocationPermissionFromUser() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            showDialoge(message = " app needs to access your location",
                posActionName = "ok",
                posAction = { dialog, which ->
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    dialog.dismiss()
                },
                negActionName = "no",
                negAction = { dialog, which ->
                    dialog.dismiss()
                })
        }
        else{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

    }

}