package com.academica.tenant.models

import java.util.*


class ServerModels (var id: Int= getAutoId(), var address: String =  "", var name : String = ""){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100);
        }
    }
}