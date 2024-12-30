package com.zj.wanandroid.ui.page.main.home.square

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.zj.wanandroid.ui.page.common.RouteName
import com.zj.wanandroid.ui.widgets.MultiStateItemView
import com.zj.wanandroid.ui.widgets.RefreshList
import com.zj.wanandroid.utils.RouteUtils

@Composable
fun SquarePage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SquareViewModel = hiltViewModel()
) {
    val viewStates = remember { viewModel.viewStates }
    val squareData = viewStates.pagingData.collectAsLazyPagingItems()
    // Optimize: 这里主要解决返回LazyColumn仍然置顶的问题，根源在于如果存在footer/header的话，LazyListState去滚动到之前的位置时，
    //  由于flow中的数据没有出来，完全就是滚不动（但是有把事件消费了），解决办法是flow中数据有的时候再次拿到的LazyListState（有记忆位置）
    //  重新触发重组（刷新UI）就行；详情解释可以看README.md
    val listState = if (squareData.itemCount > 0) viewStates.listState else LazyListState()

    RefreshList(squareData, listState = listState) {
        itemsIndexed(squareData) { _, item ->
            MultiStateItemView(
                data = item!!,
                onSelected = {
                    RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, it)
                })
        }
    }
}