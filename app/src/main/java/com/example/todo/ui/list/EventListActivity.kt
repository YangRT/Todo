package com.example.todo.ui.list


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.todo.R
import com.example.todo.databinding.ActivityListBinding
import com.example.todo.ui.list.finished.FinishedFragment
import com.example.todo.ui.list.unfinished.UnfinishedFragment

class EventListActivity : AppCompatActivity() {
    private var isFirst:Boolean = true
    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list)
        val unFinishedFragment = UnfinishedFragment()
        val finishedFragment = FinishedFragment()
        val fragmentManager:FragmentManager = supportFragmentManager
        val t:FragmentTransaction=fragmentManager.beginTransaction()
        t.add(R.id.fragment_layout,unFinishedFragment)
        t.commit()
        binding.finished.setOnClickListener {
            val transaction=fragmentManager.beginTransaction()
            transaction.hide(unFinishedFragment)
            if(isFirst){
                transaction.add(R.id.fragment_layout,finishedFragment)
                isFirst = false
            }else{
                transaction.show(finishedFragment)
            }
            transaction.commit()
        }
        binding.unfinished.setOnClickListener {
            val transaction = fragmentManager.beginTransaction()
            if(!isFirst){
                transaction.show(unFinishedFragment)
                transaction.hide(finishedFragment)
                transaction.commit()
            }
        }

    }
}
