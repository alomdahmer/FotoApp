package com.example.fotoapp.api

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.fotoapp.vo.UsuarioVO
import com.google.gson.Gson
import org.json.JSONObject

class UsuarioAPI {
    val WS_CADASTRO_USUARIO = "https://blogfefb.firebaseapp.com/users/save"
    val WS_LOGIN = "https://blogfefb.firebaseapp.com/users/login"

    //fnRetorno é a função de callback. Ela será executada quando for encerrada a execução dos métodos desta classe
    //no exemplo da linha abaixo, fnRetorno tem como parâmetros uma lista do tipo UsuarioVO e não possui retorno (Unit)
    fun cadastrarUsuario(usuario : UsuarioVO, fnRetorno: () -> Unit){
        val gson = Gson()
        val conteudo = gson.toJson(usuario)

        val req = JsonObjectRequest(
            Request.Method.POST, WS_CADASTRO_USUARIO, JSONObject(conteudo),
            Response.Listener {
                fnRetorno()
            },
            Response.ErrorListener { erro ->
                erro.printStackTrace()
            })
        APPQueue.executar(req)
    }

    fun fazerLogin (usuario : UsuarioVO, fnRetorno: (UsuarioVO) -> Unit){
        val gson = Gson()
        val conteudo = gson.toJson(usuario)
        val req = JsonObjectRequest(
            Request.Method.POST, WS_LOGIN, JSONObject(conteudo),
            Response.Listener { dados ->
                //a linha abaixo permite salvar o usuário já logado na aplicação
                val userLogado = gson.fromJson<UsuarioVO>(dados.toString(), UsuarioVO::class.java)
                fnRetorno(userLogado)
            },
            Response.ErrorListener { erro ->
                erro.printStackTrace()
            })
        APPQueue.executar(req)
    }
}