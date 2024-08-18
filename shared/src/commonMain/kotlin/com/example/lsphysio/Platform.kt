package com.example.lsphysio

interface Platform {
    var name: String
}

expect fun getPlatform(): Platform