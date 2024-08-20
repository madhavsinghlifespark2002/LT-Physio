package com.lifesparktech.lsphysio

class Greeting {
    private val platform: Platform = getPlatform()
    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    companion object {
        var name = ""
    }
}