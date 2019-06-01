package com.example.fotoapp.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object APPQueue {
    private var queue : RequestQueue? = null

    fun iniciar(context : Context){

        if (queue == null){
            queue = Volley.newRequestQueue(context);
        }
    }

    fun <T> executar(req : Request<T>){
        queue?.add(req)
    }
}
