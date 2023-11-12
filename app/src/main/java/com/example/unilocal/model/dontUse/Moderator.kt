package com.example.unilocal.model.dontUse

import com.example.unilocal.model.dontUse.Person

class Moderator(idUser:Int,
                name:String,
                last_name:String,
                email:String,
                var nickname:String,
                password:String,
                idCountry:Int,
                idDepartment:Int,
                idCity:Int,
                age:Int,
                phone: String){

    override fun toString(): String {
        return super.toString()
    }
}