package net.tapetee

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.content_gallery.*
import net.tapetee.adapter.CategoryAdapter
import net.tapetee.model.category.Category
import net.tapetee.retrofit.AsiaRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity(), Callback<Collection<Category>> {

    override fun onFailure(call: Call<Collection<Category>>, t: Throwable) {

    }

    override fun onResponse(call: Call<Collection<Category>>, response: Response<Collection<Category>>) {

        swipeToRefresh.isRefreshing = false
        categories?.addAll(response.body()!!)
        adapterCategory?.notifyDataSetChanged()

    }


    private var page: Int = 1
    private var perPage: Int = 10

    private var adapterCategory: CategoryAdapter? = null

    private var layoutManager: GridLayoutManager? = null

    private var categories: ArrayList<Category>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(toolbar)

        title = getString(R.string.category)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        AsiaRetrofit.create().getCategories(page, perPage).enqueue(this)


        swipeToRefresh.isRefreshing = true

        categories = ArrayList()
        adapterCategory = CategoryAdapter(this, categories as ArrayList)
        layoutManager = GridLayoutManager(this, 2)

        listCategory.adapter = adapterCategory
        listCategory.layoutManager = GridLayoutManager(this, 2)

        swipeToRefresh.setOnRefreshListener {
            page = 1
            categories = ArrayList()
            AsiaRetrofit.create().getCategories(page, perPage).enqueue(this)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
