package com.example.unilocal.db

import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.Schedule
import com.example.unilocal.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Places {
    /*
    private val places:java.util.ArrayList<Place> = ArrayList()
    public var id:Int = 1

    init {
        val schedule1 = Schedule(1, Schedules.getAll(),12,20)
        val schedule2 = Schedule(2, Schedules.getWeekDay(),9,20)
        val schedule3 = Schedule(3, Schedules.getWeekend(),14,13)

        val place1 = Place(id, "Maria Licky", "Comidas Rápidas", 1, PlaceStatus.ACCEPTED, 3, "Cll 12B 15-02",4.542725, -75.670001,1,1,1)
        place1.schedules.add(schedule3)
        id++
        val place2 = Place(id, "Hotel San José", "El hotel más exclusivo del santander", 2, PlaceStatus.ACCEPTED, 1, "En el santander xd",4.537227, -75.672451,1,1,1)
        place2.schedules.add(schedule2)
        id++
        val place3 = Place(id, "Unicentro", "Centro Comercial unicentro", 3, PlaceStatus.REJECTED, 4, "San Agustín 22-30",4.543521, -75.685812,1,1,1)
        place3.schedules.add(schedule2)
        id++
        val place4 = Place(id, "CentralPark", "Parque Recreacional", 1, PlaceStatus.PENDING, 4, "Cra 28A 12-01",4.566316, -75.652585,1,1,1)
        place4.schedules.add(schedule1)
        id++
        val place5 = Place(id, "Bar HUB", "Barcito", 2, PlaceStatus.ACCEPTED, 5, "Freislo 88879",4.539410, -75.667842,1,1,1)
        place5.schedules.add(schedule2)
        id++
        val place6 = Place(id, "Licky Liquors", "Licorera", 3, PlaceStatus.ACCEPTED, 5, "Av. Centenario B13-9",4.531532, -75.668269,1,1,1)
        place6.schedules.add(schedule3)
        id++

        places.add(place1)
        places.add(place2)
        places.add(place3)
        places.add(place4)
        places.add(place5)
        places.add(place6)

    }

    fun ListByState(status:PlaceStatus):ArrayList<Place>{
        return places.filter { p -> p.status == status }.toCollection(ArrayList())
    }

    fun ListByOwner(idOwner:Int):ArrayList<Place>{
        return places.filter { p -> p.idOwner == idOwner }.toCollection(ArrayList())
    }

    fun get(id:Int): Place?{
        return places.firstOrNull { p -> p.id == id }
    }

    fun size (): Int {
        return places.size
    }

    fun findByName(name:String): ArrayList<Place> {
        return places.filter { p -> p.name.lowercase().contains(name.lowercase()) && p.status == PlaceStatus.ACCEPTED }.toCollection(ArrayList())
    }

    fun add (place: Place){
        id++
        places.add(place)
    }

    fun create(place:Place){
        places.add( place )
    }

    fun findByCity(idCity:Int): ArrayList<Place> {
        return places.filter { p -> p.idCity == idCity && p.status == PlaceStatus.ACCEPTED }.toCollection(ArrayList())
    }

    fun findByCategory(idCategory:Int): ArrayList<Place> {
        return places.filter { p -> p.idCategory == idCategory && p.status == PlaceStatus.ACCEPTED }.toCollection(ArrayList())
    }

    fun changeStatus(id:Int, newStatus:PlaceStatus){

        val place = places.firstOrNull{ p -> p.id == id}

        if(place!=null){
            place.status = newStatus
        }

    }
*/
}