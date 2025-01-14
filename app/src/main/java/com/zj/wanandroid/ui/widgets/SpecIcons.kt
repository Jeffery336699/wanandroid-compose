package com.zj.wanandroid.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.material.placeholder
import com.zj.wanandroid.R
import com.zj.wanandroid.theme.AppTheme
import com.zj.wanandroid.theme.white


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HotIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_hot),
        contentDescription = null,
        tint = AppTheme.colors.hot,
        modifier = modifier
            .size(20.dp)
            .pointerInteropFilter { false }
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShareIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_share),
        contentDescription = null,
        modifier = modifier
            .width(25.dp)
            .height(25.dp)
            .pointerInteropFilter { false }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FavouriteIcon(
    modifier: Modifier = Modifier,
    isFavourite: Boolean = false,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    Icon(
        imageVector = if (isFavourite && !isLoading) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint = if (isFavourite && !isLoading) AppTheme.colors.themeUi else AppTheme.colors.textSecondary,
        modifier = modifier
            .width(25.dp)
            .height(25.dp)
            .clickable(enabled = !isLoading) { onClick.invoke() }
            /**
             * pointerInteropFilter是一个低级工具，主要用于需要处理原生 Android View系统的事件时,通过pointerInteropFilter，你可以访问底层触摸事件（MotionEvent），从而实现定制化的交互或复用已有逻辑，但建议优先使用 Compose 提供的组合式手势 API，除非确实需要访问原生事件。
             * 1. 返回true表示事件被拦截并消费。
             * 2. 返回false表示事件未被消费，会继续向下传递。
             * 3. 假设你想实现一个自定义的双击手势（传统的GestureDetector）：
             * ```
             *  @Composable
             * fun DoubleTapView(onDoubleTap: () -> Unit) {
             *     val context = LocalContext.current
             *     val gestureDetector = remember {
             *         GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
             *             override fun onDoubleTap(e: MotionEvent?): Boolean {
             *                 onDoubleTap()
             *                 return true
             *             }
             *         })
             *     }
             *
             *     Box(
             *         modifier = Modifier
             *             .fillMaxSize()
             *             .pointerInteropFilter { motionEvent ->
             *                 gestureDetector.onTouchEvent(motionEvent)
             *                 true // 消费事件
             *             }
             *     )
             * }
             * ```
             */
            .pointerInteropFilter { false }
    )
}

@Composable
fun TimerIcon(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_time),
        contentDescription = "",
        tint = AppTheme.colors.textSecondary,
        modifier = modifier
            .width(15.dp)
            .height(15.dp)
            .clip(RoundedCornerShape(15.dp / 2))
            .placeholder(
                visible = isLoading,
                color = AppTheme.colors.placeholder
            )
    )
}

@Composable
fun UserIcon(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_author),
        contentDescription = "",
        modifier = modifier
            .width(15.dp)
            .height(15.dp)
            .clip(RoundedCornerShape(15.dp / 2))
            .placeholder(
                visible = isLoading,
                color = AppTheme.colors.placeholder
            ),
        tint = AppTheme.colors.textSecondary
    )
}

@Composable
fun AddIcon(
    modifier: Modifier,
    color: Color = AppTheme.colors.textPrimary
) {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        tint = color,
        modifier = modifier
    )
}

@Composable
fun NotificationIcon(
    modifier: Modifier,
    tintColor: Color = white
) {
    Icon(
        Icons.Default.Notifications,
        contentDescription = "New message",
        modifier = modifier,
        tint = tintColor
    )
}

@Composable
fun DotView(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "",
        modifier = modifier
            .size(10.dp)
            .background(color = AppTheme.colors.hot, RoundedCornerShape(5.dp)),
        color = white,
        textAlign = TextAlign.Center,
        maxLines = 1,
        fontSize = 5.sp
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Icon(
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        tint = AppTheme.colors.textSecondary,
        modifier = modifier
            .clickable {
                onClick.invoke()
            }
            .pointerInteropFilter { false }
    )
}