package com.example.fotoapp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.widget.Toast
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.example.fotoapp.R
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Vibrator
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_tirar_foto.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class TirarFotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tirar_foto)
        //showAlert()
        estaCameraHabilitada()
    }

    private var mCurrentPhotoPath : String? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_TAKE_PHOTO = 1


    //método que verifica se há permissões para a acesso à câmera por parte da aplicação
    private fun estaCameraHabilitada() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        } else
            iniciarCamera()
    }

    //cria um listener que monitora o GPS do aparelho
    fun iniciarGPS() {
        try {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    atualizarPosicao(location)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        } catch (ex: SecurityException) {
            Toast.makeText(this, "Erro de permissão do GPS", Toast.LENGTH_LONG).show()
        }
    }

    //método que atualizar os textViews enquanto o GPS também atualiza a posição
    fun atualizarPosicao(l : Location){
        var latPoint = l.latitude
        var lonPoint = l.longitude
        txtLatitude.setText(latPoint.toString())
        txtLongitude.setText(lonPoint.toString())
    }

    //método executado depois de se verificar se há permissões para utilização da câmera no aparelhos celular (executado no retorno do método cameraHabilitada())
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    iniciarCamera()
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun iniciarCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                photoFile = File.createTempFile("FotoApp", ".jpg", storageDir)
                mCurrentPhotoPath = "file:" + photoFile!!.getAbsolutePath()
            } catch (ex: IOException) {
                Toast.makeText(applicationContext, "Não foi possível obter a foto", Toast.LENGTH_SHORT).show()
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,"com.example.fotoapp.fileprovider", photoFile))
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    //executado no retorno do método iniciarCamera()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                val imagem = imgView
                val bm1 = BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(mCurrentPhotoPath)))
                imagem.setImageBitmap(bm1)
                Toast.makeText(this, "Fotografia obtida com sucesso!!", Toast.LENGTH_LONG).show()
                vibrarAparelho()
                iniciarGPS()
            } catch (fnex: FileNotFoundException) {
                Toast.makeText(applicationContext, "Foto não encontrada!", Toast.LENGTH_LONG).show()
            }

        }
    }

    //executado quando a activity TirarForoActivity é inicializada
    fun tirarFoto(v : View){
        estaCameraHabilitada()
    }

    fun vibrarAparelho(){
        var v : Vibrator
        var tempo : Long = 2000
        v =  getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(tempo)
    }

}
