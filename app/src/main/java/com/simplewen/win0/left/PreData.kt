package com.simplewen.win0.left
import android.graphics.Bitmap
import android.webkit.*
import com.simplewen.win0.R
import com.simplewen.win0.app.iwhToast

/**静态类 存储该项目的各类静态数据
 * author：iwh
 * time ：2019.01.8
 * **/
 class PreData{
   companion object {
       val jcItems = arrayOf("1.数据结构与算法","2.程序设计基础","3.软件工程基础","4.数据库设计基础")
       val jcC = arrayOf(
             arrayOf("算法","数据结构的基本概念","线性表与存储结构","栈和队列","线性链表","树与二叉树","查找技术","排序技术"),
             arrayOf("程序设计方法与风格"),
             arrayOf("软件工程基础"),
             arrayOf("数据库基础"))
       val jcItems2 = arrayOf("计算机基础")

       val chTitle = arrayListOf(
               mapOf("chNum" to 0,"title" to "Word相关练习"),
               mapOf("chNum" to 1,"title" to "Excel相关练习"),
               mapOf("chNum" to 2,"title" to "PowerPoint相关练习"))

       //存储web教程链接,word,excel,ppt
       val jcVideoBaseUrl = "https://www.borebooks.top/MAS/jcs/videos/"
       val jcUrls = arrayListOf("https://www.borebooks.top/MAS/local_html/word.html",
               "https://www.borebooks.top/MAS/local_html/excel.html",
               "https://www.borebooks.top/MAS/local_html/ppt.html")

       /**获取章节
        * @param ch 教程类型
        * @param sonKey 获取子项url
        * **/
       fun getPreUrl(ch:Int = 0) = jcUrls[ch]

       /**
        * 拼接链接
        * @param base 基础链接
        * @param num 集数
        * @param type 类型
        * @return 返回播放链接
        */
       fun getPlayUrl(num:Int,type:String) = "${this.jcVideoBaseUrl}$type$num.mov"

       /**
        * 获取集数
        * @param type 分类
        */
        fun getNum(type:Int):Int{
          return  when(type){
               0 -> 24
               1-> 27
               2-> 5
                else -> 0
            }

        }
       /**web加载方法
        * @param web webview组件
        * @param urls 链接
        * **/
       fun webAutoLoad(web: WebView, urls:String){
           web.settings.javaScriptEnabled = true
           web.loadUrl(urls)
           web.webViewClient = object :WebViewClient(){
               override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                   iwhToast("加载中！")
                   super.onPageStarted(view, url, favicon)
               }

               override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                   iwhToast("网络错误！", R.color.warn)
                   super.onReceivedError(view, request, error)
               }
               override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                   return false
               }

           }


       }
   }


}
