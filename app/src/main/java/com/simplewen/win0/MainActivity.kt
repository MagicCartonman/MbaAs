package com.simplewen.win0


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.simplewen.win0.center.iwh_fg_center
import com.simplewen.win0.left.iwh_fg_left
import com.simplewen.win0.right.iwh_fg_right
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.File

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        val temSql = mySql(this@MainActivity,"glx",1)//创建数据
        temSql.writableDatabase
        val hand: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg?.what) {
                    0 -> {
                        Toast.makeText(this@MainActivity, "复制成功", Toast.LENGTH_SHORT).show()

                    }
                    1 -> {
                        Toast.makeText(this@MainActivity, "复制失败", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(this@MainActivity, "数据库存在", Toast.LENGTH_SHORT).show()
                    }
                    3 -> {
                        Toast.makeText(this@MainActivity, "数据库不存在", Toast.LENGTH_SHORT).show()
                    }
                    4 -> {
                        Toast.makeText(this@MainActivity, "查询完成", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val list_fg = arrayListOf<Fragment>()
        val fg_left = iwh_fg_left()
        val fg_center = iwh_fg_center()
        val fg_right = iwh_fg_right()
        list_fg.add(fg_left)
        list_fg.add(fg_center)
        list_fg.add(fg_right)
        val  iwh_tab = findViewById<TabLayout>(R.id.tab)
        val iwh_viewPage = findViewById<ViewPager>(R.id.viewPage)
        var iwh_view_page_adapter = iwh_view_page_adapter(supportFragmentManager,list_fg)
        iwh_viewPage.adapter = iwh_view_page_adapter//设置viewpage的adapter

        iwh_tab.setSelectedTabIndicatorColor(Color.parseColor("#185639"))//tab下划线颜色
        iwh_tab.setTabTextColors(Color.WHITE,Color.WHITE)
        iwh_tab.addTab(iwh_tab.newTab().setText("知识体系"))
        iwh_tab.addTab(iwh_tab.newTab().setText("习题册"))
        iwh_tab.addTab(iwh_tab.newTab().setText("我的"))
        //iwh_tab.setupWithViewPager(iwh_viewPage)//将viewpage与tablayout绑定一起
        iwh_tab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                iwh_tab.getTabAt(tab.position)?.select()
                iwh_viewPage.setCurrentItem(tab.position,true)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
        //viewpage事件监听器
        iwh_viewPage.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                iwh_tab.getTabAt(position)?.select()
            }
        })


        val file = File(ImportDB.DB_PATH + "/glx")//调用伴生对象
        Log.d("here",ImportDB.DB_PATH)
        val share = getSharedPreferences("dbFlag", Activity.MODE_PRIVATE)

        if (file.exists()){
            //内部数据库存在，开始导入外部
           // Toast.makeText(this@MainActivity, "存在内部数据库", Toast.LENGTH_SHORT).show()
            if(share.getString("dbFlag","0") == "1"){
              //  Toast.makeText(this@MainActivity, "已经导入过数据库", Toast.LENGTH_SHORT).show()
                 }else{
                val inDb = ImportDB(this@MainActivity)
                if(inDb.copyDatabase()){
                   // Toast.makeText(this@MainActivity, "复制完成", Toast.LENGTH_SHORT).show()
                    val shareP = getSharedPreferences("dbFlag", Activity.MODE_PRIVATE)
                    val edit = shareP.edit()
                    edit.putString("dbFlag","1")//导入完成，设置标志
                    edit.apply()
                }else{
                    Toast.makeText(this@MainActivity, "复制失败", Toast.LENGTH_SHORT).show()
                }
            }


        }

        fab.setOnClickListener { _ ->
           val dia = AlertDialog.Builder(this@MainActivity)
            dia.setIcon(R.drawable.fab_bg)
                    .setTitle("欢迎你加入我们")
                    .setMessage("群号：726619838")
                    .setPositiveButton("确认"){
                        _,_ ->
                        Toast.makeText(this@MainActivity,"欢迎哦",Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("不了"){
                        _,_ ->
                       // Toast.makeText(this@MainActivity,"好吧",Toast.LENGTH_SHORT).show()
                    }
                    .create().show()

        }

    }

    override fun onBackPressed() {

            val intent = Intent()
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_share ->
            {
                Toast.makeText(this@MainActivity,"分享给小伙伴",Toast.LENGTH_SHORT).show()
            }
            R.id.action_about -> {
                val ab = layoutInflater.inflate(R.layout.about,null)
                val about_dia = AlertDialog.Builder(this@MainActivity)
                about_dia.setView(ab).create().show()
            }




        }
        return true
    }



}
