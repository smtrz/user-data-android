package com.tahir.tahir_assignment.models.data

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: Map<String, Any>,
    val phone: String,
    val website: String,
    val company: Map<String, Any>
)