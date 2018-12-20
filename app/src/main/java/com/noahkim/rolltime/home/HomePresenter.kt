package com.noahkim.rolltime.home

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.noahkim.rolltime.data.MatchesRepository

class HomePresenter : MviBasePresenter<HomeView, HomeViewState>() {
  override fun bindIntents() {
    val matchesObservable = MatchesRepository.observable
        .map {
          HomeViewState(it)
        }

    subscribeViewState(matchesObservable, HomeView::render)
  }
}

interface HomeView : MvpView {
  fun render(homeViewState: HomeViewState)
}