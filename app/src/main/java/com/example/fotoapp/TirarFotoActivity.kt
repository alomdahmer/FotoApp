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
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
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
        cameraHabilitada()
    }

    private var mCurrentPhotoPath : String? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_TAKE_PHOTO = 1


    fun localizacaoHabilitada(){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1);
            }
            //else
                //configurarServico();
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Habilitar GPS")
            .setMessage("Suas configurações de localização estão desligadas'.\nHabilite o GPS do aparelho para usar o FotoApp")
            .setPositiveButton("Habilitar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->

            })
        dialog.show()
    }

    //método que verifica se há permissões para a acesso à câmera por parte da aplicação
    private fun cameraHabilitada() {
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
            abrirCamera()
    }

    //método executado depois de se verificar se há permissões para utilização da câmera no aparelhos celular
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirCamera()
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun abrirCamera() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                val imagem = imgView
                val bm1 = BitmapFactory.decodeStream(contentResolver.openInputStream(Uri.parse(mCurrentPhotoPath)))
                imagem.setImageBitmap(bm1)
            } catch (fnex: FileNotFoundException) {
                Toast.makeText(applicationContext, "Foto não encontrada!", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun tirarFoto(v : View){
        cameraHabilitada()
    }

}
