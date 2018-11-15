package com.simplewen.win0.left.ch1

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import com.simplewen.win0.R

class fg_ch_two :Fragment(){
    private val url_ch_two = arrayListOf("https://www.borebooks.top/MAS/MAS_two/MAS_ch_two_1.htm",
            "https://www.borebooks.top/MAS/MAS_two/MAS_ch_two_2.htm",
            "https://www.borebooks.top/MAS/MAS_two/MAS_ch_two_3.htm",
            "https://www.borebooks.top/MAS/MAS_two/MAS_ch_two_eg.htm")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val key = arguments.getInt("ch_key")
        var dia2: AlertDialog? =null
        val dia = AlertDialog.Builder(activity).setView(R.layout.loading).setCancelable(false).create()
        var vi = inflater.inflate(R.layout.fg_ch_two_1,container,false)
        fun webLoad(web:WebView,urls:String){


            val wt = web.settings
            wt.setJavaScriptEnabled(true)
            web.webChromeClient =object : WebChromeClient(){

            }
            wt.allowContentAccess =true
            wt.allowFileAccess = true
            wt.allowFileAccessFromFileURLs = true
            web.loadUrl(urls)
            web.webViewClient = object : WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    //  Toast.makeText(activity,"加载中",Toast.LENGTH_SHORT).show()
                    dia2 = dia
                    dia.show()
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    dia2?.dismiss()
                    // Toast.makeText(activity,"fg",Toast.LENGTH_SHORT).show()
                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    Toast.makeText(activity,"网络错误", Toast.LENGTH_SHORT).show()
                    super.onReceivedError(view, request, error)
                }
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return false
                }

            }

        }
        when(key){
            0 -> {
                vi = inflater.inflate(R.layout.fg_ch_two_1,container,false)
                val web = vi.findViewById<WebView>(R.id.fg_ch_two_web_1)
                webLoad(web,url_ch_two[0])
            }
            1 -> {
                vi = inflater.inflate(R.layout.fg_ch_two_2,container,false)
                val web = vi.findViewById<WebView>(R.id.fg_ch_two_web_2)
                webLoad(web,url_ch_two[1])
            }
            2 -> {
                vi = inflater.inflate(R.layout.fg_ch_two_3,container,false)
                val web = vi.findViewById<WebView>(R.id.fg_ch_two_web_3)
                webLoad(web,url_ch_two[2])
            }
            3 -> {
                vi  = inflater.inflate(R.layout.fg_ch_two_eg,container,false)
                val web = vi.findViewById<WebView>(R.id.fg_ch_two_web_eg)
                webLoad(web,url_ch_two[3])
            }

        }
        return vi
    }
}