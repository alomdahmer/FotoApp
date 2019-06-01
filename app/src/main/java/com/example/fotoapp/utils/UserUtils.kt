package com.example.fotoapp.utils

import com.example.fotoapp.vo.UsuarioVO

object UserUtils {

    private var user : UsuarioVO? = null

    fun setUser(u : UsuarioVO){
        user = u
    }

    fun getUser() : UsuarioVO? {
        return user
    }
}