package com.lifesparktech.lsphysio
interface Platform {
    var name: String
}


expect fun getPlatform(): Platform