package com.example.unilocal.model

class Category() {

    constructor(id:Int, name:String, icon:String ):this(){
        this.id = id
        this.name = name
        this.icon = icon
    }

    var id:Int = 0
    var key:String = ""
    var name:String = ""
    var icon:String = ""

    override fun toString(): String {
        return "Category(id=$id, name='$name', icon='$icon')"
    }
}