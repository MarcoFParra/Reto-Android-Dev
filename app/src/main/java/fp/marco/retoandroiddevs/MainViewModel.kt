package fp.marco.retoandroiddevs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fp.marco.retoandroiddevs.Model.Place
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainViewModel: ViewModel() {
    val baseURL = "https://fintecimal-test.herokuapp.com/api/interview/"
    val places = MutableLiveData<List<Place>?>()
    val connectionError = MutableLiveData<Boolean>()


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        connectionError.postValue(true)
    }


    fun getPlaces()
    {
        try {
            viewModelScope.launch(exceptionHandler) {
                val call = getRetrofit().create(APIService::class.java).getAllPlaces(baseURL)
                if (call.isSuccessful)
                {
                    places.postValue(call.body())
                }
                else connectionError.postValue(true)

            }
        }catch (e: Exception){
            connectionError.postValue(true)

        }

    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}