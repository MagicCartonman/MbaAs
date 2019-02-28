package com.simplewen.win0.left.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.*
import com.simplewen.win0.R
import com.simplewen.win0.app.iwhToast
import kotlinx.android.synthetic.main.activity_jc_show.*
/**本地html输出**/
class jcShow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jc_show)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val jcWeb = findViewById<WebView>(R.id.jcWebView)
        jcWeb.webViewClient = object:WebViewClient(){
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
            }
        }

        val routeKey = if(intent.getStringExtra("jcKey") == "") {
            iwhToast("数据出错！")
            finish()
            null
        }else {
            intent.getStringExtra("jcKey")}
        routeKey?.let {
            jcWeb.loadDataWithBaseURL(null,null,"text/html", "utf-8", null)
            jcWeb.loadUrl("file:///android_asset/local_html/${routeKey}")
           // iwhToast("加载：$routeKey")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
