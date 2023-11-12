package com.example.unilocal.model

import java.time.LocalDateTime
import java.util.Date

class Comment() {

    var text:String = ""
    var idUser:String = ""
    var rating:Int = 0
    var key:String = ""
    var date:Date = Date()
    constructor(text:String, idUser:String, rating:Int):this(){
        this.text = text
        this.idUser = idUser
        this.rating = rating
    }


    override fun toString(): String {
        return "Comment(text='$text', idUser=$idUser, rating=$rating, date=$date)"
    }


}