package fp.marco.retoandroiddevs

import fp.marco.retoandroiddevs.Model.Place
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET suspend fun getAllPlaces(@Url url:String):Response<List<Place>>
}