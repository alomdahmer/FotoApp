package com.example.fotoapp

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.fotoapp.api.UsuarioAPI
import com.example.fotoapp.utils.UserUtils
import com.example.fotoapp.vo.UsuarioVO
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import android.location.LocationListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        localizacaoHabilitada()
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Habilitar GPS")
            .setMessage("Suas configurações de localização podem estar desligadas.\nVerifique se o serviço de localização está ativo para poder utilizar o FotoApp")
            .setPositiveButton("Verificar", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->

            })
        dialog.show()
    }

    //solicita a autorização de acesso ao GPS do usuário na primeira execução do aplicativo
    fun localizacaoHabilitada(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            showAlert()
            ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1);
        }
    }

    //método executado quando do retorno do método localizacaoHabilitada()
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    //iniciarGPS()
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    fun fazerLogin(v : View){
        val u = UsuarioVO();
        u.login = txtNomeUsuario.text.toString();
        u.senha = txtSenha.text.toString();
        val api = UsuarioAPI();

        api.fazerLogin(u) {userLogado ->
            if (ckManterConectado.isChecked){
                //a linha abaixo cria o sharedPreferences da aplicação (algo parecido com o LocalStorage do navegador)
                //Context.MODE_PRIVATE significa que somente a aplicação terá acesso ao sharedPreferences chamado de "fotos_prefs"
                val prefs = getSharedPreferences("fotos_prefs", Context.MODE_PRIVATE)
                val gson = Gson()
                //monta o objeto do usuário logado no sharedPreferences
                prefs.edit().putString("user", gson.toJson(userLogado)).commit()

            }
            UserUtils.setUser(userLogado)
            val i = Intent(this, TirarFotoActivity::class.java)
            startActivityForResult(i, 1)
        }
    }

    fun cadastrarNovoUsuario(v : View){
        val i = Intent(this, NovoUsuarioActivity::class.java)
        startActivityForResult(i, 1)
    }

    fun tiraFotoDireto(v : View){
        val i = Intent(this, TirarFotoActivity::class.java)
        startActivity(i)
    }

}