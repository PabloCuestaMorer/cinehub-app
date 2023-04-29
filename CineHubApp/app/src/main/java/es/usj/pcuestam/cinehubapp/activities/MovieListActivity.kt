package es.usj.pcuestam.cinehubapp.activities


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.usj.pcuestam.cinehubapp.R
import es.usj.pcuestam.cinehubapp.adapters.MovieListAdapter
import es.usj.pcuestam.cinehubapp.viewmodels.MovieListViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope


class MovieListActivity : AppCompatActivity() {

    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        movieListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MovieListViewModel::class.java)
        // Use lifecycleScope to call the suspend function
        lifecycleScope.launch {
            movieListViewModel.loadMovieList()
        }

        // Observe changes to the movie list data and update the adapter accordingly
        movieListViewModel.movieListLiveData.observe(this, Observer { movieList ->
            movieListAdapter = MovieListAdapter(movieList) { movie ->
                // Handle movie click
                Toast.makeText(this, "Clicked: ${movie.title}", Toast.LENGTH_SHORT).show()
            }

            movieRecyclerView = findViewById(R.id.movieRecyclerView)
            movieRecyclerView.layoutManager = LinearLayoutManager(this)
            movieRecyclerView.adapter = movieListAdapter
        })

        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "Movie List"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movie_list, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieListAdapter.filter.filter(newText)
                return true
            }
        })

        val filterItem = menu.findItem(R.id.action_filter)
        filterItem.setOnMenuItemClickListener {
            showFilterDialog()
            true
        }
        return true
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_filter_options, null)
        val yearEditText = dialogView.findViewById<EditText>(R.id.yearEditText)
        val ratingEditText = dialogView.findViewById<EditText>(R.id.ratingEditText)

        AlertDialog.Builder(this)
            .setTitle("Filter Options")
            .setView(dialogView)
            .setPositiveButton("Apply") { _, _ ->
                val year = yearEditText.text.toString().toIntOrNull()
                val rating = ratingEditText.text.toString().toFloatOrNull()
                movieListAdapter.applyFilters(year, rating)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}