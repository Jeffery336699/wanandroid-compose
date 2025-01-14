package com.zj.wanandroid.ui.page.main.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zj.wanandroid.theme.AppTheme
import com.zj.wanandroid.theme.BottomNavBarHeight
import com.zj.wanandroid.ui.page.main.category.navi.NaviPage
import com.zj.wanandroid.ui.page.main.category.pubaccount.PublicAccountPage
import com.zj.wanandroid.ui.page.main.category.stucture.StructurePage
import com.zj.wanandroid.ui.widgets.TextTabBar

import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryPage(
    navCtrl: NavHostController,
    categoryIndex: Int = 0,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    DisposableEffect(Unit) {
        onDispose {
            // Optimize: 页面虽然销毁了，但是ViewModel采用hiltViewModel()创建的话，
            //  存在于navigation#BackStack的viewModelStore中，再说其BackStack做了saveState = true的处理，即viewModel不会被销毁;
            //  如果把Commons#BottomNavBarView底部跳转逻辑的saveState、restoreState设置为false，那么viewModel的onCleared()方法就会被调用
            /**
             *  日志如下：
             *  I  2333333 CategoryViewModel ==> onClear
             *  I  2333333  CategoryPage onDispose
             */
            println("2333333  CategoryPage onDispose")
        }
    }
    val titles = viewModel.titles
    Box(modifier = Modifier.padding(bottom = BottomNavBarHeight)) {
        Column {
            val pagerState = rememberPagerState(
                initialPage = categoryIndex,
            )
            val scopeState = rememberCoroutineScope()

            Row {
                TextTabBar(
                    index = pagerState.currentPage,
                    tabTexts = titles,
                    modifier = Modifier.weight(1f),
                    contentAlign = Alignment.CenterStart,
                    onTabSelected = { index ->
                        scopeState.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }

            HorizontalPager(
                count = titles.size,
                state = pagerState,
                modifier = Modifier.background(AppTheme.colors.background)
            ) { page ->
                when (page) {
                    0 -> StructurePage(navCtrl)
                    1 -> NaviPage(navCtrl)
                    2 -> PublicAccountPage(navCtrl)
                }
            }
        }
    }
}
