package com.noahkim.rolltime.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noahkim.rolltime.R
import com.noahkim.rolltime.adapters.MatchHolder.ClickListener
import com.noahkim.rolltime.data.Match

class MatchAdapter(var userBeltPreference: String)
  : RecyclerView.Adapter<MatchHolder>() {

  private val beltArray = intArrayOf(R.drawable.ic_bjj_white_belt, R.drawable.ic_bjj_blue_belt,
      R.drawable.ic_bjj_purple_belt, R.drawable.ic_bjj_brown_belt, R.drawable.ic_bjj_black_belt)

  var matchesList: List<Match>? = null
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.matches_list_item, parent,
        false)
    val matchHolder = MatchHolder(view)
    matchHolder.setOnClickListener(object : ClickListener {
      override fun onItemClick(view: View?, position: Int) {
        //start activity here for EditMatch
      }

      override fun onItemLongClick(view: View?, position: Int) {}
    })
    return matchHolder
  }

  override fun onBindViewHolder(holder: MatchHolder, position: Int) {
    val match = matchesList!![position]
    holder.apply {
      setUserBeltLevel(beltArray[userBeltPreference.toInt()])
      setOppName(match.oppName)
      setOppBeltLevel(beltArray[match.oppBeltLevel])
      setUserChokeCount(match.userChokeCount)
      setUserArmlockCount(match.userArmlockCount)
      setUserLeglockCount(match.userLeglockCount)
      setOppChokeCount(match.oppChokeCount)
      setOppArmlockCount(match.oppArmlockCount)
      setOppLeglockCount(match.oppLeglockCount)
    }
  }

  override fun getItemCount() = matchesList?.size ?: 0
}