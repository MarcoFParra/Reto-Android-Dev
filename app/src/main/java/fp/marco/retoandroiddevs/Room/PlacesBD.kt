package fp.marco.retoandroiddevs.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import fp.marco.retoandroiddevs.Model.Place

@Database(
    entities = [Place::class],
    version = 1
)

abstract class PlacesBD : RoomDatabase() {
    abstract fun placeDao() : PlaceDao
}