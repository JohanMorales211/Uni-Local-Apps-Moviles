package com.example.unilocal.db

import com.example.unilocal.model.User

object Users {
/*
    private var id:Int = 1
    private val users:java.util.ArrayList<User> = ArrayList()

    init {
        users.add(User(id, "Andres", "Ocampo", "loca@gmail.com", "Z4ND3R", "helado444", 1, 1, 1, 20,"","3213456789"))
        id++
        users.add(User(id, "Santiago", "Bernal", "loca2@gmail.com", "ElSantiago", "1234", 1, 1, 1, 20,"","3206340436"))
        id++
        users.add(User(id, "Luisa", "Pulido", "loca3@gmail.com", "Lu", "4321", 1, 2, 2, 22,"", "3207449924"))
        id++
    }

    fun list():ArrayList<User>{
        return users
    }

    fun findNameByID(id:Int): String?{
        try {
            var user= users.first { u -> u.id == id }
            return user.name
        } catch (e:Exception){
            return null
        }
    }

    fun findByEmail(email:String): User? {
        try {
            var user= users.first { u -> u.email == email }
            return user
        }catch (e:Exception){
            return null
        }
    }

    fun findByUsername(username:String): User? {
        try {
            var user= users.first { u -> u.nickname == username }
            return user
        }catch (e:Exception){
            return null
        }
    }

    fun findByPhone(phone:String): User? {
        try {
            var user= users.first { u -> u.phone == phone }
            return user
        }catch (e:Exception) {
            return null
        }
    }

    fun add (user: User){
        id++
        users.add(user)
    }

    fun size (): Int {
        return users.size
    }

    /*fun login(email:String, pass:String):User{
        val answer = users.first { u -> u.password == pass && u.email == email }
        return answer
    }


 */
 */
}