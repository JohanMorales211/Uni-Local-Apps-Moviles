package com.example.unilocal.db

import com.example.unilocal.model.Schedule
import com.example.unilocal.model.WeekDay

object Schedules {

    val list:ArrayList<Schedule> = ArrayList()

    fun getAll():ArrayList<WeekDay>{
        val everyDay:ArrayList<WeekDay> = ArrayList()
        everyDay.addAll( WeekDay.values() )
        return everyDay
    }

    fun getWeekend():ArrayList<WeekDay>{
        val everyDay:ArrayList<WeekDay> = ArrayList()
        everyDay.add(WeekDay.FRIDAY)
        everyDay.add(WeekDay.SATURDAY)

        return everyDay
    }

    fun getWeekDay():ArrayList<WeekDay>{
        val everyDay:ArrayList<WeekDay> = ArrayList()
        everyDay.add( WeekDay.MONDAY )
        everyDay.add( WeekDay.TUESDAY )
        everyDay.add( WeekDay.WEDNESDAY )
        everyDay.add( WeekDay.THURSDAY )
        everyDay.add( WeekDay.FRIDAY )

        return everyDay
    }
}