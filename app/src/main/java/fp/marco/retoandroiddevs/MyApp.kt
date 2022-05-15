package fp.marco.retoandroiddevs

import android.app.Application
import androidx.room.Room
import fp.marco.retoandroiddevs.Room.PlacesBD

class MyApp : Application() {

    public lateinit var room: PlacesBD

    companion object {
        lateinit var instance: MyApp
            private set
    }



    override fun onCreate() {
        super.onCreate()
        instance = this
        room = Room
            .databaseBuilder(instance,PlacesBD::class.java,"places")
            .build()
    }



}