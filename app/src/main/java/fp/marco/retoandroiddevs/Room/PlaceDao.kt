package fp.marco.retoandroiddevs.Room

import androidx.lifecycle.LiveData
import androidx.room.*
import fp.marco.retoandroiddevs.Model.Place

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(places: List<Place>)

    @Update
    suspend fun update(place: Place)

    @Query("DELETE from Place")
    suspend fun removeAll()

    @Query("UPDATE Place set visited = 1 where id = :id ")
    suspend fun setAsVisited(id:Int)

    @Query("SELECT * from Place")
    fun getAll(): LiveData<List<Place>>

    @Query("SELECT * from Place where id = :id ")
    fun getByID(id:Int): Place

    @Query("SELECT count(*) from Place where visited = :visited")
    suspend fun getCountByStatus(visited:Boolean): Int
}