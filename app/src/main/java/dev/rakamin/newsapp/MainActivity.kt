package dev.rakamin.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rakamin.newsapp.adapter.NewsAdapter
import dev.rakamin.newsapp.databinding.ActivityMainBinding
import dev.rakamin.newsapp.model.Article
import dev.rakamin.newsapp.model.NewsResponse
import dev.rakamin.newsapp.network.NewsInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val listArticles = mutableListOf<Article>()
    private var currentPage = 1
    private var isFetching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. DISINI Menambahkan setSupportActionBar
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        //fetchHeadlines()
        fetchMoreNews(currentPage)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(listArticles)
        // Mengakses RecyclerView di dalam contentMain
        val rvNews = binding.contentMain.rvAllNews

        rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

                    if (!isFetching && lastVisibleItem == listArticles.size - 1) {
                        currentPage++
                        fetchMoreNews(currentPage)
                    }
                }
            })
        }
    }

    private fun getService(): NewsInterface {
        return Retrofit.Builder()
            .baseUrl(NewsInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsInterface::class.java)
    }

    private fun fetchHeadlines() {
        getService().getHeadlines().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    if (!articles.isNullOrEmpty()) {
                        val mainNews = articles[0]
                        binding.contentMain.tvHeadlineTitle.text = mainNews.title
                        Glide.with(this@MainActivity)
                            .load(mainNews.urlToImage)
                            .placeholder(R.drawable.logo_bank_mandiri)
                            .error(R.drawable.logo_bank_mandiri)
                            .centerCrop()
                            .into(binding.contentMain.ivHeadline)
                    }
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                android.util.Log.e("API_ERROR", t.message ?: "Unknown Error")
            }
        })
    }

    private fun fetchMoreNews(page: Int) {
        isFetching = true
        getService().getEverything(page = page).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                isFetching = false
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    if (!articles.isNullOrEmpty()) {


                        if (page == 1) {
                            val mainNews = articles[0]
                            binding.contentMain.tvHeadlineTitle.text = mainNews.title

                            Glide.with(this@MainActivity)
                                .load(mainNews.urlToImage)
                                .placeholder(R.drawable.logo_bank_mandiri)
                                .error(R.drawable.logo_bank_mandiri)
                                .centerCrop()
                                .into(binding.contentMain.ivHeadline)
                        }


                        newsAdapter.addData(articles)
                    }
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                isFetching = false
            }
        })
    }
}