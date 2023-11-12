package com.example.unilocal.model

import android.net.Uri
import com.example.unilocal.db.Comments
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class Place() {


    var key:String = ""
    var name:String = ""
    var description:String = ""
    var idOwner:String = ""
    var status: PlaceStatus? = null
    var idCategory:Int = 0
    var direction:String = ""
    var latitude:Double = 0.0
    var longitude:Double = 0.0
    var idCountry:Int = 0
    var idDepartment:Int = 0
    var idCity:Int = 0

    var images: MutableList<String> = mutableListOf()
    var phoneNumbers:ArrayList<String> = ArrayList()
    var date: Date = Date()
    var schedules:ArrayList<Schedule> = ArrayList()

    constructor(name:String, description:String, idOwner:String,
                status: PlaceStatus, idCategory:Int, direction:String, latitude:Double,
                longitude:Double,idCountry:Int, idDepartment:Int, idCity:Int):this(){

        this.name = name
        this.description = description
        this.idOwner = idOwner
        this.status = status
        this.idCategory = idCategory
        this.direction = direction
        this.latitude = latitude
        this.longitude = longitude
        this.idCountry = idCountry
        this.idDepartment = idDepartment
        this.idCity = idCity
    }

    fun isOpen():String{
        val date = Calendar.getInstance()
        val day = date.get(Calendar.DAY_OF_WEEK)
        val hour = date.get(Calendar.HOUR_OF_DAY)

        var message = "isClosed"

        for(schedule in schedules){
            if (schedule.weekDay.contains( WeekDay.values()[day - 1]) && hour < schedule.closingTime && hour > schedule.startTime){
                message = "Open"
                break
            }
        }
        return message
    }

    fun getClosingTime():String{
        val date = Calendar.getInstance()
        val day = date.get(Calendar.DAY_OF_WEEK)

        var message = ""

        for(schedule in schedules){
            if (schedule.weekDay.contains(WeekDay.values()[day - 1])){
                message = schedule.closingTime.toString()
                break
            }
        }
        return message
    }

    fun getRatingAverage(comments:ArrayList<Comment>):Int{
        var average = 0

        if(comments.size > 0) {
            val sum = comments.stream().map { c -> c.rating }
                .reduce { sum, valor -> sum + valor }.get()

            average = sum/comments.size
        }

        return average
    }

    override fun toString(): String {
        return "Place(name='$name', description='$description', idOwner=$idOwner, status=$status, idCategory=$idCategory, direction=${direction}, latitude=$latitude, longitude=$longitude, idCountry=$idCountry, idDepartment=$idDepartment, idCity=$idCity, images=$images, phoneNumbers=$phoneNumbers, date=$date, schedules=$schedules)"
    }

}