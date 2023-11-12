package com.example.unilocal.model

class User(){
constructor(name:String, last_name:String, nickname:String, email:String,
            idCountry:Int, idDepartment:Int, idCity:Int, age:Int, imgUrl:String, phone: String, rol:Rol):this(){

                this.name = name
                this.last_name = last_name
                this.nickname = nickname
                this.idCountry = idCountry
                this.idDepartment = idDepartment
                this.idCity = idCity
                this.age = age
                this.phone = phone
                this.imgUrl = imgUrl
                this.rol = rol
                this.email = email
            }

    var name:String = ""
    var last_name:String = ""
    var email:String = ""
    var idCountry:Int = 0
    var idDepartment:Int = 0
    var idCity:Int = 0
    var age:Int = 0
    var phone:String = ""
    var rol:Rol = Rol.USER
    var key:String = ""
    var nickname:String  = ""
    var imgUrl:String  = ""

    override fun toString(): String {
        return "Usuario(nickname='$nickname') "
    }
}