package com.gunalirezqi.moviecatalogue.feature.favorite.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.adapter.FavoriteTvShowAdapter
import com.gunalirezqi.moviecatalogue.feature.detail.tvshow.DetailTvShowActivity
import com.gunalirezqi.moviecatalogue.helper.databaseTvShow
import com.gunalirezqi.moviecatalogue.model.FavoriteTvShow
import kotlinx.android.synthetic.main.fragment_tv_show.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class TvShowFragment : Fragment() {

    private var favorites: MutableList<FavoriteTvShow> = mutableListOf()
    private lateinit var adapter: FavoriteTvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter
        list_tv_show.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavoriteTvShowAdapter(favorites) {
            startActivity<DetailTvShowActivity>(
                DetailTvShowActivity.EXTRA_TVSHOW_ID to it.tvShowId,
                DetailTvShowActivity.EXTRA_TVSHOW_NAME to it.tvShowName
            )
        }

        list_tv_show.adapter = adapter
        swipeRefresh.onRefresh {
            showFavorite()
        }

        showFavorite()
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    private fun showFavorite() {
        favorites.clear()
        context?.databaseTvShow?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTvShow.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteTvShow>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }

        message_tv_show.text =
            if (favorites.isEmpty()) resources.getString(R.string.empty_favorites_tv_shows) else ""
    }
}
