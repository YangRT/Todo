package com.example.todo.ui.list.finished

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.Toast
import com.example.todo.databinding.FragmentEventListBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.EventRepository
import com.example.todo.data.model.Datas
import com.example.todo.data.model.Event
import com.example.todo.data.network.ToDoNetwork
import com.example.todo.ui.list.Adapter
import com.example.todo.ui.list.EventListActivity
import com.example.todo.ui.list.event.AddEventActivity
import com.example.todo.ui.list.event.EventDetailActivity
import com.example.todo.ui.login.LoginActivity
import com.example.todo.utils.PopupWindowFactory
import com.example.todo.utils.Type
import kotlinx.android.synthetic.main.fragment_event_list.*
import kotlinx.android.synthetic.main.popup_window.view.*
import kotlinx.android.synthetic.main.popup_window3.view.*

class FinishedFragment : Fragment(),AdapterView.OnItemSelectedListener,View.OnClickListener {

    private lateinit var viewModel:FinishedListViewModel
    var list:ArrayList<Datas> = ArrayList()
    private lateinit var adapter: Adapter
    private lateinit var myDataBinding:FragmentEventListBinding
    private var page = 1
    private var deletePos = -1
    private var choicePos = -1
    private lateinit var myWindow:PopupWindow
    private lateinit var myWindow2:PopupWindow

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myDataBinding = FragmentEventListBinding.inflate(inflater,container,false)
        viewModel = ViewModelProviders.of(this,FinishedViewModelFactory(EventRepository.getInstance(ToDoNetwork.getInstance()))).get(FinishedListViewModel::class.java)
        myDataBinding.viewModel = viewModel
        return myDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

    }

    private fun init(){
        finished_add.setOnClickListener(this)
        finished_more.setOnClickListener(this)
        myWindow = PopupWindowFactory.createWindow(context,Type.ACCOUNT_WINDOW)
        myWindow2 = PopupWindowFactory.createWindow(context,Type.FINISHED_EVENT_WINDOW)
        myWindow.contentView.window_text1.setOnClickListener(this)
        myWindow.contentView.window_text2.setOnClickListener(this)
        myWindow2.contentView.window3_text1.setOnClickListener(this)
        myDataBinding.finishedListSpinner.onItemSelectedListener = this
        myDataBinding.finishedRecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = Adapter(list)
        adapter.setListener(object :Adapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                showWindow(Type.FINISHED_EVENT_WINDOW)
                deletePos = position
            }
        })
        adapter.setViewListener(object :Adapter.OnViewClickListener{
            override fun onItemClick(position: Int) {
                choicePos = position
                showEventDetail()
            }
        })
        myDataBinding.finishedRecyclerView.adapter = adapter
        myDataBinding.finishedRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1)){
                    page++
                    viewModel.getInfo(page,0,null,null)
                }
            }
        })
        observeData()
    }


    private fun observeData(){
        viewModel.info.observe(this, Observer {
            val code = it.errorCode
            if(code == 0){
                if(it.data.datas.size != 0){
                    list.addAll(it.data.datas)
                    adapter.notifyDataSetChanged()
                }else{
                    page--
                    Toast.makeText(context,"没有更多数据", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context,it.errorMsg, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.deleteInfo.observe(this, Observer {
            var code = it.errorCode
            if(code == 0){
                list.removeAt(deletePos)
                adapter.notifyItemRemoved(deletePos)

            }else{
                Toast.makeText(context,it.errorMsg, Toast.LENGTH_LONG).show()
            }
            if(myWindow2.isShowing){
                myWindow2.dismiss()
            }
        })
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.finished_more -> showWindow(Type.ACCOUNT_WINDOW)
            R.id.finished_add -> {
                val i = Intent(activity, AddEventActivity::class.java)
                startActivityForResult(i,1)
            }
            R.id.window_text1 -> dealWithAccount(Type.CHANGE_ACCOUNT)
            R.id.window2_text2 -> dealWithAccount(Type.EXIT)
            R.id.window3_text1 -> {
                viewModel.deleteInfo(list[deletePos].id)
            }
        }
    }

    private fun dealWithAccount(type:Int){
        when(type){
            Type.EXIT -> activity?.finish()
            Type.CHANGE_ACCOUNT ->{
                var intent = Intent(activity, LoginActivity::class.java)
                startActivityForResult(intent,2)
            }
        }
        if(myWindow.isShowing){
            myWindow.dismiss()
        }
    }

    private fun showEventDetail(){
        var i = Intent(activity, EventDetailActivity::class.java)
        i.putExtra("title",list[choicePos].title)
        i.putExtra("content",list[choicePos].content)
        i.putExtra("type",list[choicePos].type)
        i.putExtra("id",list[choicePos].id)
        i.putExtra("status",1)
        startActivity(i)
    }

    private fun showWindow(type: Int){
        when(type){
            Type.ACCOUNT_WINDOW -> myWindow.showAtLocation(this.view, Gravity.CENTER,0,0)
            Type.FINISHED_EVENT_WINDOW -> myWindow2.showAtLocation(this.view, Gravity.CENTER,0,0)
        }
        val lp = (context as Activity).window.attributes
        lp.alpha = 0.4f; //设置透明度
        (context as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        (context as Activity).window.attributes = lp;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && resultCode == 1){
            var i = Intent(activity, EventListActivity::class.java)
            startActivity(i)
            activity?.finish()
        }else if(requestCode == 2 && resultCode==1){
            activity?.finish()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        page = 1
        list.clear()
        when (position) {
            0 -> viewModel.getInfo(page, 1, null, null)
            Type.LIFE -> viewModel.getInfo(page, 1, 1, null)
            Type.WORK -> viewModel.getInfo(page, 1, 2, null)
            Type.LEARN -> viewModel.getInfo(page, 1, 3, null)
            Type.OTHER -> viewModel.getInfo(page, 1, 4, null)
        }
    }
}