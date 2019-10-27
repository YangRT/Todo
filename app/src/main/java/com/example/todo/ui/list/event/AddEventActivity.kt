package com.example.todo.ui.list.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.todo.R
import com.example.todo.data.EventRepository
import com.example.todo.data.network.ToDoNetwork
import com.example.todo.utils.Type
import kotlinx.android.synthetic.main.activity_add_event.*
import java.util.*


class AddEventActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var binding: com.example.todo.databinding.EventDetailBinding
    private lateinit var viewModel: EventViewModel
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        binding = DataBindingUtil.setContentView(this,R.layout.event_detail)
        viewModel = ViewModelProviders.of(this,EventViewModelFactory(EventRepository.getInstance(ToDoNetwork.getInstance()))).get(EventViewModel::class.java)
        binding.viewmodel = viewModel
        init()
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_commit -> checkCommit()
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
//            R.id.add_event_back -> {
//                setResult(0)
//                finish()
//            }
        }
    }

    private fun init(){
        viewModel.detail.set(true)
        viewModel.life.set(false)
        viewModel.work.set(false)
        viewModel.learn.set(false)
        viewModel.other.set(false)
        binding.btCommit.setOnClickListener(this)
        binding.tvEventTypeLife.setOnClickListener(this)
        binding.tvEventTypeWork.setOnClickListener(this)
        binding.tvEventTypeLearn.setOnClickListener(this)
        binding.tvEventTypeOther.setOnClickListener(this)
       // add_event_back.setOnClickListener(this)
    }

    private fun checkCommit() {
        if(TextUtils.isEmpty(binding.edEventTitle.text) || TextUtils.isEmpty(binding.edEventContent.text)){
            Toast.makeText(this,"请填齐信息", Toast.LENGTH_LONG).show()
            return
        }
        if(viewModel.life.get() || viewModel.work.get() || viewModel.learn.get() || viewModel.other.get()){
            viewModel.addEvent(binding.edEventTitle.text.toString(),binding.edEventContent.text.toString(),null,type)
            viewModel.addInfo.observe(this,androidx.lifecycle.Observer {
                val errorCode = it.errorCode
                if(errorCode == 0){
                    Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show()
                    setResult(1)
                    finish()
                }else{
                    Toast.makeText(this,it.errorMsg,Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(this,"请选择类型",Toast.LENGTH_SHORT).show()
        }

    }
}
