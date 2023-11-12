package com.example.unilocal.db

import com.example.unilocal.model.Comment

object Comments {

    private val list:ArrayList<Comment> = ArrayList()

    init {
        /*list.add( Comment(1, "Muy bueno, la verdad muy bueno xd", 1, 5, 1))
        list.add( Comment(2, "No, que pereza esos manes", 2, 2, 2))
        list.add( Comment(3, "Casi se me sale un ojo ese día", 3, 2, 3))
        list.add( Comment(4, "Muy barato y todo muy melo", 1, 5, 2))
        list.add( Comment(5, "Yo ni sé que escribir aquí pero bueno ", 2, 4, 1))
        list.add( Comment(6, "Terrible la atención al cliente", 1, 1, 3))
        list.add( Comment(7, "Me pareció un lugar muy agradable para estar con la familia", 1, 5, 1))
        list.add( Comment(8, "Muy bueno, la verdad muy bueno xd", 3, 5, 2))*/
    }

    /*fun listById(idPlace:Int):ArrayList<Comment>{
        return list.filter { c -> c.id == idPlace }.toCollection(ArrayList())
    }*/

    fun new(comment: Comment){
        list.add(comment)
    }

    fun getSize():Int{
        return list.size
    }
}