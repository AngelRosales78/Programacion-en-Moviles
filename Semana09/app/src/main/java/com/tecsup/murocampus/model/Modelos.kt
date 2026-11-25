package com.tecsup.murocampus.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Lo que se envía en el POST: no lleva id porque lo asigna el servidor. */
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)
