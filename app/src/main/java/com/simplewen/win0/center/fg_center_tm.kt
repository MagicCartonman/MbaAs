package com.simplewen.win0.center

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.simplewen.win0.R
import com.simplewen.win0.modal.mySql
import com.simplewen.win0.app.iwhToast
import java.lang.IllegalStateException
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.simplewen.win0.app.App
import kotlinx.android.synthetic.main.fg_center_tm.*
import java.lang.Exception


/**实现题目的Fragment
 * author：iwh
 * time：2018.10
 * 实现题目的显示，错题自动保存，添加收藏，错题集的显示与收藏显示**/
class fg_center_tm_fg:Fragment(){
    interface Callbacks{
        fun onRemove(position:Int)

    }

        private  var mcallbacks:Callbacks?= null//回调
        /**获取题目内部图片**/
        inner class getImg{
            /**设置单选图片
             * @param tm_key 指定radioButton
             * @return 返回本类实现链式调用**/
          fun setImg(tm_key:RadioButton):getImg{
                "(?<=@##)[\\s\\S]*?(?=[\$][\$]@)".toRegex().find(tm_key.text.toString())?.value?.let {
                    val radio_img_res = resources.assets.open("ms_imgs/$it")
                    try {
                        val radio_key_img = Drawable.createFromStream(radio_img_res,null)
                        radio_key_img.setBounds(0,0,radio_key_img.intrinsicWidth*3,radio_key_img.intrinsicHeight*3)
                        tm_key.setCompoundDrawables(null,null,null,radio_key_img)
                        tm_key.text = tm_key.text.toString().replace("@##${it}$$@","")
                    }catch (e:Exception){
                        iwhToast("$e")
                    }finally {
                        radio_img_res.close()
                    }


                }
                return getImg()

            }
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val ags = arguments

            val position = arguments.getInt("position")//获取FG下标
            val fab_type = arguments.getString("fab_type")
            val temSql = mySql(activity, "glx", 1)//初始化数据库链接
            val db = temSql.writableDatabase//获取读写对象
            val vi = inflater?.inflate(R.layout.fg_center_tm,container,false)
            val tm_img = vi!!.findViewById<ImageView>(R.id.tm_img)//题目图片
            val tm_content =    vi.findViewById<TextView>(R.id.tm_content)
            val tm_answer_box = vi.findViewById<LinearLayout>(R.id.tm_answer_box)
            val tm_answer_desc  = vi.findViewById<TextView>(R.id.tm_answer_desc)
            val tm_answer_right = vi.findViewById<TextView>(R.id.tm_answer_right)
            val tm_select_group = vi.findViewById<RadioGroup>(R.id.tm_select_group)
            val tm_answer_btn = vi.findViewById<Switch>(R.id.tm_answer_btn)


            tm_content.text =  if(position == 19) {
                "最后一题：${ags.getString("tm_content").replace("<br/>", "")}"
            }else {
                "第${position + 1}题：${ags.getString("tm_content").replace("<br/>", "")}"
            }

                //设置题目图片
                val tm_img_path = "(?<=@##)[\\s\\S]*?(?=[\$][\$]@)".toRegex().find(tm_content.text.toString())?.value
                tm_img_path?.let {
                    val tm_img_res = resources.assets.open("ms_imgs/$it")
                    val bitmap = BitmapFactory.decodeStream(tm_img_res)
                    tm_img_res.close()
                    tm_img.setImageBitmap(bitmap)
                    tm_img.visibility = View.VISIBLE
                    tm_content.text = tm_content.text.toString().replace("@##${it}$$@","")
                }

            tm_answer_right.text = "正确答案：" +  ags.getString("tm_answer")
            tm_answer_desc.text =  ags.getString("tm_answer_desc").replace("<br/>","")


            val id = ags.getString("tm_id")//获取id
            val right_answer = ags.getString("tm_answer")//正确答案
            val my_like_btn = vi.findViewById<FloatingActionButton>(R.id.my_like)//添加收藏按钮
            if(fab_type == "add"){
                my_like_btn.setOnClickListener{
                    val dia = AlertDialog.Builder(activity)
                    dia.setTitle("添加收藏?").setMessage("稍后可以在我的收藏页面查看该题目")
                            .setPositiveButton("确认"){
                                _,_ ->
                                val res =  temSql.wen_update(db,"like",id)
                                if(res != 0) iwhToast("收藏完成") else iwhToast("收藏失败！")

                            }.setNegativeButton("取消",null).create().show()
                }
            }else if(fab_type == "delete"){
                my_like_btn.setImageResource(R.drawable.my_delete)
                my_like_btn.setOnClickListener{
                    val res =  temSql.wen_update(db,"delete",id)
                    if(res == 0){
                        Toast.makeText(activity,"移除失败:$id,res:$res",Toast.LENGTH_SHORT).show()
                    }else{
                         Toast.makeText(activity,"移除完成",Toast.LENGTH_SHORT).show()
                        //调用接口方法！！
                        mcallbacks!!.onRemove(position)
                    }
                }
            }
            if(App._bt_flag == "1"){
                tm_answer_btn.visibility = View.GONE
                tm_answer_btn.isChecked = true
                tm_answer_box.visibility = View.VISIBLE
            }else{
                tm_answer_btn.isChecked = false
                tm_answer_box.visibility = View.GONE
                tm_answer_btn.visibility = View.VISIBLE
            }
            //获取查看解析按钮
            tm_answer_btn.setOnClickListener{
                if(tm_answer_btn.isChecked){
                    tm_answer_box.visibility = View.VISIBLE
                }else{
                    tm_answer_box.visibility = View.GONE
                }
            }
            fun my_error():Boolean{
                //自动保存错题到数据库
                iwhToast("回答错误！!", Gravity.BOTTOM, R.color.warn)
                val res =  temSql.wen_update(db,"error",id)
                if(res ==0 ){
                    iwhToast("添加失败！", Gravity.BOTTOM, R.color.warn)
                    return false
                }
                return true

            }
            //获取单选组
            tm_select_group.setOnCheckedChangeListener{
              _,checkedId ->
                var tmFlag = false
                when(checkedId){
                    R.id.tm_a -> {

                        if(right_answer != "A"){
                            tmFlag = true
                        }
                    }
                    R.id.tm_b ->{
                        if(right_answer != "B"){
                            tmFlag = true
                        }
                    }
                    R.id.tm_c -> {
                        if(right_answer != "C"){
                            tmFlag = true
                        }
                    }
                    R.id.tm_d -> {
                        if(right_answer != "D"){
                            tmFlag = true
                        }
                    }

                }
                if(tmFlag){
                    my_error()//错题添加数据库
                } else{
                    iwhToast("回答正确！", Gravity.BOTTOM)
                }
                //是否自动打开解析！
                 tm_answer_btn.isChecked = true
                tm_answer_box.visibility = View.VISIBLE

            }


            return vi
        }

    override fun onAttach(context: Context?) {
        if(context !is Callbacks){
            throw IllegalStateException("接口未实现")
        }
        super.onAttach(context)
        mcallbacks = context

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val ags = arguments
        super.onViewCreated(view, savedInstanceState)
        tm_a.text = ags.getString("tm_a").replace("<br/>","")
        tm_b.text =  ags.getString("tm_b").replace("<br/>","")
        tm_c.text = ags.getString("tm_c").replace("<br/>","")
        tm_d.text = ags.getString("tm_d").replace("<br/>","")
        //设置单选图片
        getImg().setImg(tm_a).setImg(tm_b).setImg(tm_c).setImg(tm_d)


    }





    }
