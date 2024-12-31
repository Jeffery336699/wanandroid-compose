package com.zj.wanandroid.ui.page.main.home.recommend

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zj.wanandroid.common.paging.simplePager
import com.zj.wanandroid.data.bean.Article
import com.zj.wanandroid.data.http.HttpService
import com.zj.wanandroid.ui.page.main.home.square.PagingArticle
import com.zj.wanandroid.ui.widgets.BannerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    private var service: HttpService,
) : ViewModel() {
    private val pager by lazy {
        simplePager {
            service.getIndexList(it)
        }.cachedIn(viewModelScope)
    }
    var viewStates by mutableStateOf(RecommendViewState(pagingData = pager))
        private set

    init {
        dispatch(RecommendViewAction.FetchData)
    }

    fun dispatch(action: RecommendViewAction) {
        when (action) {
            is RecommendViewAction.FetchData -> fetchData()
            is RecommendViewAction.Refresh -> refresh()
        }
    }

    private fun fetchData() {
        val imageListFlow = flow {
            kotlinx.coroutines.delay(2000)
            emit(service.getBanners())
        }.map { bean ->
            val result = mutableListOf<BannerData>()
            bean.data?.forEach {
                result.add(BannerData(it.title ?: "", it.imagePath ?: "", it.url ?: ""))
            }
            result
        }
        val topListFlow = flow {
            emit(service.getTopArticles())
        }.map {
            it.data ?: emptyList()
        }
        viewModelScope.launch {
            imageListFlow.zip(topListFlow) { banners, tops ->
                viewStates =
                    viewStates.copy(imageList = banners, topArticles = tops, isRefreshing = false)
            }.onStart {
                viewStates = viewStates.copy(isRefreshing = true)
                /**
                 * 1. 在Flow中，catch操作符在没有异常的情况下是不会执行其代码块的
                 * 2. 如果你想无论Flow成功或异常都执行某些逻辑，可以使用onCompletion操作符
                 * 3. 如果内部发生异常，就算在外面用catch操作符捕获了异常，最终程序仍然会终止
                 *  （可以理解为你没有把根源地方try..catch操作，发现时已经是表现出来的外像了）
                 */
            }.onCompletion {
                viewStates = viewStates.copy(isRefreshing = false)
            }.collect()
        }
    }

    private fun refresh() {
        fetchData()
    }

}

data class RecommendViewState(
    val pagingData: PagingArticle,
    val isRefreshing: Boolean = false,
    val imageList: List<BannerData> = emptyList(),
    val topArticles: List<Article> = emptyList(),
    val listState: LazyListState = LazyListState()
)

sealed class RecommendViewAction {
    object FetchData : RecommendViewAction()
    object Refresh : RecommendViewAction()
}