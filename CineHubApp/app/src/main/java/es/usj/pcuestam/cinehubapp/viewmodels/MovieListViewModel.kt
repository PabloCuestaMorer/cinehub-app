package es.usj.pcuestam.cinehubapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.usj.pcuestam.cinehubapp.beans.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.nio.charset.Charset

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val _movieListLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    // To expose public read-only version of the LiveData
    val movieListLiveData: LiveData<List<Movie>> = _movieListLiveData

    suspend fun loadMovieList() {
        withContext(Dispatchers.IO) {
            val jsonString = loadJsonFromAsset("movie_list.json")
            try {
                val jsonArray = JSONArray(jsonString)
                val movieList = mutableListOf<Movie>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val movie = Movie.fromJson(jsonObject)
                    movieList.add(movie)
                }
                _movieListLiveData.postValue(movieList)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun loadJsonFromAsset(fileName: String): String {
        val context = getApplication<Application>().applicationContext
        var jsonString = ""
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            jsonString = String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return jsonString
    }
}