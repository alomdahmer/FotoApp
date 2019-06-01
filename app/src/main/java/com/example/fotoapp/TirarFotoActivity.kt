package com.example.fotoapp

import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog

class TirarFotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tirar_foto)
        showAlert()
    }

    fun isLocationEnabled(): Boolean? {
        val locationManager : LocationManager? = null
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Habilitar GPS")
            .setMessage("Suas configurações de localização estão desligadas'.\nHabilite o GPS para usar o FotoApp")
            .setPositiveButton("Habilitar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }
}
