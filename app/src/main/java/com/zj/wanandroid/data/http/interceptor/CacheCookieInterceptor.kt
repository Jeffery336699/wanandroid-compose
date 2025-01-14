package com.zj.wanandroid.data.http.interceptor

import com.zj.wanandroid.data.store.DataStoreUtils
import okhttp3.Interceptor
import okhttp3.Response

class CacheCookieInterceptor: Interceptor {

    private val loginUrl = "user/login"
    private val registerUrl = "user/register"
    private val SET_COOKIE_KEY = "set-cookie"

    /**
     * Optimize: 注意看这是第一次login（有清除app本地缓存进行的）成功的响应结果，这里的cookie是服务端返回的
     *  <<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-
     *  响应 Header:{content-type=[application/json;charset=UTF-8]}
     *  响应 Header:{date=[Mon, 30 Dec 2024 18:35:50 GMT]}
     *  响应 Header:{server=[Apache-Coyote/1.1]}
     *  响应 Header:{set-cookie=[loginUserName=Jeffery; Expires=Wed, 29-Jan-2025 18:35:50 GMT; Path=/, token_pass=3f22f3fd7309b179a34e421b959493b2; Expires=Wed, 29-Jan-2025 18:35:50 GMT; Path=/, loginUserName_wanandroid_com=Jeffery; Domain=wanandroid.com; Expires=Wed, 29-Jan-2025 18:35:50 GMT; Path=/, token_pass_wanandroid_com=3f22f3fd7309b179a34e421b959493b2; Domain=wanandroid.com; Expires=Wed, 29-Jan-2025 18:35:50 GMT; Path=/]}
     *  响应 Header:{transfer-encoding=[chunked]}
     *
     *  {"data":{"admin":false,"chapterTops":[],"coinCount":85,"collectIds":[26254],"email":"","icon":"","id":5569,"nickname":"Jeffery","password":"","publicName":"Jeffery","token":"","type":0,"username":"Jeffery"},"errorCode":0,"errorMsg":""}
     *  <<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val domain = request.url().host()
        if (aboutUser(requestUrl)) {
            val cookies = response.headers(SET_COOKIE_KEY)
            if (cookies.isNotEmpty()) {
                //cookie可能有多个，都保存下来
                DataStoreUtils.putSyncData(domain, encodeCookie(cookies))
            }
        }
        return response
    }

    private fun aboutUser(url: String): Boolean = url.contains(loginUrl) or url.contains(registerUrl)
}

/**
 * 整理cookie
 */
private fun encodeCookie(cookies: List<String>): String {
    val sb = StringBuilder()
    val set = HashSet<String>()
    cookies
        .map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
        .forEach { it ->
            it.filterNot { set.contains(it) }.forEach { set.add(it) }
        }

    val ite = set.iterator()
    while (ite.hasNext()) {
        val cookie = ite.next()
        sb.append(cookie).append(";")
    }

    val last = sb.lastIndexOf(";")
    if (sb.length - 1 == last) {
        sb.deleteCharAt(last)
    }

    return sb.toString()
}
