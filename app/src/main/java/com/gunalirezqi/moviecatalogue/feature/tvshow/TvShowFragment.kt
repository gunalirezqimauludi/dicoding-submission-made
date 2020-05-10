package com.gunalirezqi.moviecatalogue.feature.tvshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.adapter.TvShowAdapter
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.feature.detail.tvshow.DetailTvShowActivity
import com.gunalirezqi.moviecatalogue.model.TvShow
import com.gunalirezqi.moviecatalogue.utils.invisible
import com.gunalirezqi.moviecatalogue.utils.visible
import kotlinx.android.synthetic.main.fragment_tv_show.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class TvShowFragment : Fragment(), TvShowView {

    private lateinit var presenter: TvShowPresenter
    private lateinit var adapter: TvShowAdapter

    private var tvShow: MutableList<TvShow> = mutableListOf()

    companion object {
        private const val STATE_TVSHOW = "state_tvshow"
    }

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
        adapter = TvShowAdapter(tvShow) {
            startActivity<DetailTvShowActivity>(
                DetailTvShowActivity.EXTRA_TVSHOW_ID to it.tvShowId,
                DetailTvShowActivity.EXTRA_TVSHOW_NAME to it.tvShowName
            )
        }
        list_tv_show.adapter = adapter

        if (savedInstanceState == null) {
            val request = ApiRepository()
            val gson = Gson()

            // Presenter
            presenter = TvShowPresenter(this@TvShowFragment, request, gson)
            presenter.getTvShowList()
        } else {
            val data = savedInstanceState.getParcelableArrayList<TvShow>(STATE_TVSHOW)
            if (data != null) showTvShowList(data)
        }

        swipeRefresh.onRefresh {
            presenter.getTvShowList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_TVSHOW, ArrayList(tvShow))
    }

    fun handleNotFound() {
        message_tv_show.text = getString(R.string.empty_tv_shows)
    }

    override fun showLoading() {
        progressbar_tv_show.visible()
    }

    override fun hideLoading() {
        progressbar_tv_show.invisible()
    }

    override fun showTvShowList(data: List<TvShow>) {
        swipeRefresh.isRefreshing = false
        tvShow.clear()
        tvShow.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
