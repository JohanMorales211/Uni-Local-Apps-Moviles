package com.example.unilocal.model

class Schedule() {

    var id:Int = 0
    var weekDay:ArrayList<WeekDay> = ArrayList()
    var startTime:Int = 0
    var closingTime:Int = 0

    constructor(id:Int, weekDay:ArrayList<WeekDay>, startTime:Int, closingTime:Int):this(){
        this.id = id
        this.weekDay = weekDay
        this.startTime = startTime
        this.closingTime = closingTime
    }

    override fun toString(): String {
        return "Schedule(id=$id, weekDay=$weekDay, startTime=$startTime, closingTime=$closingTime)"
    }
}