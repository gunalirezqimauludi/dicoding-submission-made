package com.gunalirezqi.moviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.model.TvShow
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tv_show.view.*

class TvShowAdapter(private val items: List<TvShow>, private val listener: (TvShow) -> Unit) :
    RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TvShowViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_tv_show, viewGroup, false)
        return TvShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    inner class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(
            items: TvShow,
            listener: (TvShow) -> Unit
        ) {
            with(itemView) {

                // Set Poster
                tv_show_poster.clipToOutline = true
                Picasso.get().load("https://image.tmdb.org/t/p/original/${items.tvShowPoster}")
                    .into(tv_show_poster)

                // Set Title
                tv_show_name.text = items.tvShowName

                // Set Release Date
                if (items.tvShowFirstAirDate.isNullOrEmpty()) {
                    tv_show_first_air_date.text = resources.getString(R.string.empty_release_date)
                } else {
                    tv_show_first_air_date.text = items.tvShowFirstAirDate?.substring(0, 4)
                }

                // Set RatingBar
                tv_show_vote_average.text = items.tvShowVoteAverage
                tv_show_ratingbar_vote_average.rating =
                    (items.tvShowVoteAverage?.toFloat()?.div(10)?.times(5)) ?: 0.0F

                // Set Overview
                if (items.tvShowOverview.isNullOrBlank()) {
                    tv_show_overview.text = resources.getString(R.string.empty_overview)
                } else {
                    tv_show_overview.text = items.tvShowOverview
                }

                itemView.setOnClickListener {
                    listener(items)
                }
            }
        }
    }
}