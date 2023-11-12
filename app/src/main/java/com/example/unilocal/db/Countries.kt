package com.example.unilocal.db

import com.example.unilocal.model.Country

object Countries {

    private var id:Int = 1
    private val countries:java.util.ArrayList<Country> = ArrayList()

    init {
        countries.add(Country(1, "Colombia"))
    }

    fun list():ArrayList<Country>{
        return countries
    }

    fun add (country: Country){
        countries.add(country)
    }

    fun findByID(id:Int): String?{
        try {
            var country= Countries.countries.first { c -> c.id == id }
            return country.name
        } catch (e:Exception){
            return null
        }
    }

}