package com.simplewen.win0.left.view.fragment


import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.simplewen.win0.R
import com.simplewen.win0.left.modal.PreData
import com.simplewen.win0.app.iwhToast

/**章节知识的Fragment重写**/
class fg_ch_eg :Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val chKey = arguments.getInt("chKey")
        var vi = inflater.inflate(R.layout.fg_ch_one,container,false)

        try {
            vi = inflater.inflate(R.layout.fg_ch_one,container,false)
            val web = vi.findViewById<WebView>(R.id.fg_ch_one_web)
            web?.let{
                PreData.webAutoLoad(web, PreData.getPreUrl(chKey) )
            }

        } catch (e:NullPointerException){
            iwhToast(e.printStackTrace().toString(), R.color.warn)
        }
       return vi
    }

}