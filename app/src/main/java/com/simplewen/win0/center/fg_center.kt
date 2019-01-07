package com.simplewen.win0.center


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import com.simplewen.win0.R
import com.simplewen.win0.R.color.colorPrimaryDark
import com.simplewen.win0.mySql
import java.util.*

class iwh_fg_center: Fragment(){


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater?.inflate(R.layout.fg_center,container,false)
        val listView = vi?.findViewById<ListView>(R.id.fg_center_listview)
        val refresh = vi?.findViewById<SwipeRefreshLayout>(R.id.fg_center_refresh)//获取下拉组件
        refresh?.setColorSchemeResources(colorPrimaryDark)

        val temSql = mySql(activity,"glx",1)
        var sjs = temSql.sjs
        var adapter = SimpleAdapter(activity,sjs,R.layout.fg_center_listview, arrayOf("name","id"),intArrayOf(R.id.fg_center_listview_name))
        listView?.adapter = adapter
        val db = temSql.writableDatabase
        val handle = object :Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message?) {

                when(msg!!.what){
                    0 -> {

                        sjs = temSql.sjs
                        adapter.notifyDataSetChanged()

                    }
                    1 -> {
                        temSql.wen_query(db,this,"sj","")
                        refresh?.isRefreshing = false
                    }

                }
            }
        }

        temSql.wen_query(db,handle,"sj","")
        listView?.onItemClickListener = AdapterView.OnItemClickListener {
            _,_,position,_->
           // Toast.makeText(activity,"你点击是：$position",Toast.LENGTH_SHORT).show()
            Log.d("position:",sjs.get(position).get("id").toString())
            val intent = Intent(activity, mian_tm::class.java)
            intent.putExtra("sj_id",position+1)
            intent.putExtra("fab_type","add")
            startActivity(intent)

        }
        refresh?.setOnRefreshListener {
                    val msg  = Message()
                    msg.what = 1
                    handle.sendMessage(msg)

        }
        return vi
    }
}