package com.example.fotoapp

import android.content.Context
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.DialogInterface
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.content.Intent
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.fotoapp.api.UsuarioAPI
import com.example.fotoapp.utils.UserUtils
import com.example.fotoapp.vo.UsuarioVO
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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