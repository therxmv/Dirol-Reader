package com.therxmv.dirolreader.data.entity

import com.therxmv.dirolreader.domain.models.UserModel

data class UserEntity(
    val firstName: String,
    val lastName: String,
    val avatarPath: String,
)

fun UserEntity.toDomain() = UserModel(
    firstName,
    lastName,
    avatarPath,
)