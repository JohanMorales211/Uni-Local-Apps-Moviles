package com.example.unilocal.db

import android.util.Log
import com.example.unilocal.model.*
import com.example.unilocal.model.dontUse.Person
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object People {
/*
    fun login(email:String): Person?{
        var result: Person? = null

        Firebase.firestore
            .collection("users")
            .get()
            .addOnSuccessListener {
                for(doc in it) {
                    var user = doc.toObject(User::class.java)
                    Log.e("People", "User bien")
                    if(user.email == email){
                        result = user
                    }
                }
            }
            .addOnFailureListener {

            }

        Firebase.firestore
            .collection("admins")
            .get()
            .addOnSuccessListener {
                for(doc in it) {
                    var admin = doc.toObject(Administrator::class.java)
                    if(admin.email == email){
                        result = admin
                    }
                }
            }
            .addOnFailureListener {

            }

        Firebase.firestore
            .collection("moderators")
            .get()
            .addOnSuccessListener {
                for(doc in it) {
                    var moder = doc.toObject(Moderator::class.java)
                    if(moder.email == email){
                        result = moder
                    }
                }
            }
            .addOnFailureListener {

            }*/
        /*if(result == null){
            result = Moderators.list().firstOrNull { u -> u.password == password && u.email == email }
            if(result == null){
                result = Administrators.list().firstOrNull { u -> u.password == password && u.email == email }
            }
        }*/

}