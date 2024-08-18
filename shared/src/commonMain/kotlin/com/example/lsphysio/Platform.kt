package com.example.lsphysio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform