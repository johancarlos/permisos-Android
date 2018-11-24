package com.example.estudiante.permisos

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    companion object {
        private val EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100
        private val REQUEST_PERMISSION_SETTING = 101
        private var sentToSettings = false
        private var permissionStatus: SharedPreferences? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
    }

    fun validarPermisos(view: View) {

        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Necesitamos permisos de almacenamiento")
                builder.setMessage("Esta aplicación necesita permiso de almacenamiento.")
                builder.setPositiveButton("Grant", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_CONSTANT)
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                builder.show()
            } else if (permissionStatus!!.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Esta aplicación necesita permiso de almacenamiento.")
                builder.setMessage("Esta aplicación necesita permiso de almacenamiento.")
                builder.setPositiveButton("Grant", DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    sentToSettings = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.setData(uri)
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(baseContext, "Ir a la configuración de permisos.", Toast.LENGTH_LONG).show()
                })
                builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                builder.show()
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_CONSTANT)
            }

            val editor = permissionStatus!!.edit()
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true)
            editor.commit()


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission()
        }
    }

    private fun proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(baseContext, "Tenemos el permiso de Almacenamiento", Toast.LENGTH_LONG).show()
    }


}
