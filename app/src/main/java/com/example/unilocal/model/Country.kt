package com.example.unilocal.model

class Country() {
    var id:Int = 0
    var name:String = ""
    var key:String = ""

    constructor(id:Int, name:String):this(){
        this.name = name
        this.id = id
    }
}