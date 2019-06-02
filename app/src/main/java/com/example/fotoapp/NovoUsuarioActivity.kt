package com.example.fotoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.fotoapp.api.UsuarioAPI
import com.example.fotoapp.vo.UsuarioVO
import kotlinx.android.synthetic.main.activity_novo_usuario.*

class NovoUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_usuario)
    }

    fun cadastrarUsuario(v : View){
        val u = UsuarioVO()
        u.nome = txtNome.text.toString()
        u.email = txtEmail.text.toString()
        u.login = txtLogin.text.toString()
        u.senha = txtSenha.text.toString()
        val api = UsuarioAPI()
        api.cadastrarUsuario(u) {
            val i = Intent(this, TirarFotoActivity::class.java)
            startActivityForResult(i, 1)
        }
    }
}
