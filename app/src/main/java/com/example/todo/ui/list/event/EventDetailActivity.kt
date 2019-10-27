package com.example.todo.ui.list.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.todo.R
import com.example.todo.data.EventRepository
import com.example.todo.data.network.ToDoNetwork
import com.example.todo.utils.Type
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: com.example.todo.databinding.EventDetailBinding
    private lateinit var viewModel: EventViewModel
    private lateinit var title:String
    private lateinit var content:String
    private var type:Int = -1
    private var id = -1
    private var status = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        title = intent.extras.getString("title","")
        content = intent.extras.getString("content","")
        type = intent.extras.getInt("type")
        id = intent.extras.getInt("id")
        status = intent.extras.getInt("status")
        if(status == 1){

        }
        binding = DataBindingUtil.setContentView(this,R.layout.event_detail)
        viewModel = ViewModelProviders.of(this,EventViewModelFactory(EventRepository.getInstance(ToDoNetwork.getInstance()))).get(EventViewModel::class.java)
        binding.viewmodel = viewModel
        observeData()
        init()
        when(type){
            Type.LIFE -> viewModel.life.set(true)
            Type.WORK -> viewModel.work.set(true)
            Type.LEARN -> viewModel.learn.set(true)
            Type.OTHER -> viewModel.other.set(true)

        }

    }

    private fun init(){
        viewModel.detail.set(false)
        viewModel.life.set(false)
        viewModel.work.set(false)
        viewModel.learn.set(false)
        viewModel.other.set(false)
        binding.btCommit.setOnClickListener(this)
        binding.edEventContent.setText(content)
        binding.edEventTitle.setText(title)

        // add_event_back.setOnClickListener(this)
    }

    private fun observeData(){

        viewModel.updateInfo.observe(this, Observer {
            var code = it.errorCode
            if(code == 0){
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show()
                var i = Intent()
                i.putExtra("title",binding.edEventTitle.text.toString())
                i.putExtra("content",binding.edEventContent.text.toString())
                i.putExtra("type",type)
                setResult(1,i)
                finish()
            }else{
                Toast.makeText(this,it.errorMsg,Toast.LENGTH_SHORT).show()

            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_event_type_life -> {
                type = Type.LIFE
                viewModel.life.set(true)
                viewModel.work.set(false)
                viewModel.learn.set(false)
                viewModel.other.set(false)
            }
            R.id.tv_event_type_work ->{
                type = Type.WORK
                viewModel.life.set(false)
                viewModel.work.set(true)
                viewModel.learn.set(false)
                viewModel.other.set(false)
            }
            R.id.tv_event_type_learn ->{
                type = Type.LEARN
                viewModel.life.set(false)
                viewModel.work.set(false)
                viewModel.learn.set(true)
                viewModel.other.set(false)
            }
            R.id.tv_event_type_other ->{
                type = Type.OTHER
                viewModel.life.set(false)
                viewModel.work.set(false)
                viewModel.learn.set(false)
                viewModel.other.set(true)
            }
            R.id.bt_commit -> commitInfo()
        }
    }

    private fun commitInfo(){
        if(TextUtils.isEmpty(binding.edEventTitle.text) || TextUtils.isEmpty(binding.edEventContent.text)){
            Toast.makeText(this,"请填齐信息", Toast.LENGTH_LONG).show()
            return
        }
        viewModel.detail.set(false)
        viewModel.updateEvent(id,binding.edEventTitle.text.toString(),binding.edEventContent.text.toString(),getNow(),0,type)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var m = menuInflater.inflate(R.menu.event_detail,menu)
        if(status == 1){
            var m = menu
            var i = m?.findItem(R.id.edit)
            i?.title = ""
        }
        return true
    }

    fun getNow(): String {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return SimpleDateFormat("yyyy-MM-dd").format(Date())
        } else {
            var tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString()

        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(status==1){
            return super.onOptionsItemSelected(item)
        }
        if(item != null){
            if(item.title.equals("编辑")){
                item.title = "正在编辑"
            }else{
                item.title = "编辑"
            }
        }
        viewModel.detail.set(!viewModel.detail.get())
        if(viewModel.detail.get()){
            binding.tvEventTypeLife.setOnClickListener(this)
            binding.tvEventTypeWork.setOnClickListener(this)
            binding.tvEventTypeLearn.setOnClickListener(this)
            binding.tvEventTypeOther.setOnClickListener(this)
        }else{
            binding.tvEventTypeLife.isClickable = false
            binding.tvEventTypeWork.isClickable = false
            binding.tvEventTypeLearn.isClickable = false
            binding.tvEventTypeOther.isClickable = false
        }
        return super.onOptionsItemSelected(item)
    }
}
