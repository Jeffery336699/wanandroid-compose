package com.zj.wanandroid.ui.page.webview

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.zj.wanandroid.data.bean.WebData
import com.zj.wanandroid.theme.ToolBarHeight
import com.zj.wanandroid.ui.widgets.AppToolsBar
import com.zj.wanandroid.utils.RouteUtils.back
import com.zj.wanandroid.utils.SizeUtils


@SuppressLint("UseCompatLoadingForDrawables")
@Composable
fun WebViewPage(
    webData: WebData,
    navCtrl: NavHostController
) {
    var ctrl: WebViewCtrl? by remember { mutableStateOf(null) }
    Box {
        var isRefreshing: Boolean by remember { mutableStateOf(false) }
        val refreshState = rememberSwipeRefreshState(isRefreshing)
        AndroidView(
            modifier = Modifier
                .padding(top = ToolBarHeight)
                .fillMaxSize(),
            factory = { context ->
                FrameLayout(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
                    )

                    // 2. 创建 ProgressBar
                    val progressBar =
                        ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)

                    // 3. 设置布局参数
                    var layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,  // 宽度：内容大小
                        SizeUtils.dp2px(2f) // 高度：内容大小
                    )/*.apply {
                        setMargins(0,20,0,0)
                    }*/

                    layoutParams.gravity = Gravity.TOP // 居中放置
                    // 设置背景颜色的一种方式
                    progressBar.backgroundTintList = ColorStateList.valueOf(Color.LTGRAY) // 设置背景颜色
                    progressBar.progressTintList = ColorStateList.valueOf(Color.BLUE) // 设置主进度颜色
                    progressBar.layoutParams = layoutParams

                    val webView = WebView(context).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                    addView(webView)
                    addView(progressBar)
                    ctrl = WebViewCtrl(this, webData.url, onWebCall = { isFinish ->
                        isRefreshing = !isFinish
                    })
                    ctrl?.initSettings()

                    // Optimize: 可以解开这里注释玩玩progressBar的效果
                    /*thread {
                        var progress = 0
                        while (progress < 100) {
                            progress += 10
                            progressBar.progress = progress
                            postInvalidate()
                            Thread.sleep(1000)
                        }
                        progressBar.visibility = View.GONE
                    }*/
                }

            },
            update = {

            }
        )

        AppToolsBar(title = webData.title ?: "标题", onBack = {
            ctrl?.onDestroy()
            navCtrl.back()
        })
    }
}