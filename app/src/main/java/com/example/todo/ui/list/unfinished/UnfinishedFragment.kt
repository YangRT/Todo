package com.example.todo.ui.list.unfinished
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.EventRepository
import com.example.todo.data.model.Datas
import com.example.todo.data.network.ToDoNetwork
import com.example.todo.databinding.FragmentUnfinishedBinding
import com.example.todo.ui.list.Adapter
import com.example.todo.ui.list.event.AddEventActivity
import com.example.todo.ui.list.event.EventDetailActivity
import com.example.todo.ui.login.LoginActivity
import com.example.todo.utils.PopupWindowFactory
import com.example.todo.utils.Type
import kotlinx.android.synthetic.main.fragment_unfinished.*
import kotlinx.android.synthetic.main.popup_window.view.*
import kotlinx.android.synthetic.main.popup_window2.view.*

class UnfinishedFragment: Fragment(),AdapterView.OnItemSelectedListener,View.OnClickListener {
    //    private val viewModel: UnfinishedListViewModel by lazy { ViewModelProviders.of(this).get(
    //    UnfinishedListViewModel::class.java) }
    private lateinit var viewModel:UnfinishedListViewModel
    private var list = ArrayList<Datas>()
    private lateinit var adapter: Adapter
    lateinit var myDataBinding:FragmentUnfinishedBinding
    private var page = 1
    private var type = -1
    private var needClear = false
    private lateinit var myWindow: PopupWindow
    private lateinit var myWindow2: PopupWindow
    private var deletePos:Int = -1
    private var updateStatusPos= -1
    private var updateEventPos = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myDataBinding = FragmentUnfinishedBinding.inflate(inflater,container,false)
        viewModel = ViewModelProviders.of(this,UnfinishedViewModelFactory(EventRepository.getInstance(ToDoNetwork.getInstance()))).get(UnfinishedListViewModel::class.java)
        myDataBinding.viewModel = viewModel
        return myDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    //初始化
    private fun init(){
        myWindow = PopupWindowFactory.createWindow(context,Type.ACCOUNT_WINDOW)
        myWindow2 = PopupWindowFactory.createWindow(context,Type.UNFINISHED_EVENT_WINDOW)
        myWindow.contentView.window_text1.setOnClickListener(this)
        myWindow.contentView.window_text2.setOnClickListener(this)
        myWindow2.contentView.window2_text1.setOnClickListener(this)
        myWindow2.contentView.window2_text2.setOnClickListener(this)
        myDataBinding.imageAdd.setOnClickListener(this)
        myDataBinding.more.setOnClickListener(this)
        myDataBinding.unfinishedListSpinner.onItemSelectedListener = this
        myDataBinding.unfinishedRecyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        adapter = Adapter(list)
        adapter.setListener(object :Adapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                updateStatusPos = position
                deletePos = position
                showPopupWindow(Type.UNFINISHED_EVENT_WINDOW)

            }
        })
        adapter.setViewListener(object :Adapter.OnViewClickListener{

            override fun onItemClick(position: Int) {
                updateEventPos = position
                showEventDetail()
            }
        })
        myDataBinding.unfinishedRecyclerView.adapter = adapter
        myDataBinding.unfinishedRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1)){
                    page++
                    viewModel.getInfo(page,0,null,null)
                }
            }
        })
        myDataBinding.unfinishedRefresh.setOnRefreshListener {
            page = 1
            needClear = true
            if (type > 0){
                viewModel.getInfo(page,0,type,null)
            }else{
                viewModel.getInfo(page,0,null,null)
            }
        }
       observeData()
    }

    private fun observeData(){
        //观察事件列表数据
        viewModel.info.observe(this, Observer {
            val code = it.errorCode
            if(code == 0){
                if(it.data.datas.size != 0){
                    if(needClear){
                        list.clear()
                    }
                    list.addAll(it.data.datas)
                    adapter.notifyDataSetChanged()
                }else{
                    page--
                    Toast.makeText(context,"没有更多数据", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(context,it.errorMsg, Toast.LENGTH_LONG).show()
            }
            needClear = false
            if(myDataBinding.unfinishedRefresh.isRefreshing){
                myDataBinding.unfinishedRefresh.isRefreshing = false
            }
        })
        //观察更新反馈数据
        viewModel.updateInfo.observe(this, Observer {
            val code = it.errorCode
            if(code == 0){
                Log.e("unfinished","${list.size} ")
                list.removeAt(updateStatusPos)
                Log.e("unfinished","${list.size} ")
                adapter.notifyItemRemoved(updateStatusPos)
                adapter.notifyItemRangeChanged(updateStatusPos,list.size+1-updateStatusPos)
                if(myWindow2.isShowing){
                    myWindow2.dismiss()
                }
            }else{
                Toast.makeText(context,it.errorMsg, Toast.LENGTH_LONG).show()
            }
        })
        //观察删除反馈信息
        viewModel.deleteInfo.observe(this, Observer {
            val code = it.errorCode
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
            R.id.image_add -> {
                val i = Intent(activity,AddEventActivity::class.java)
                startActivityForResult(i,1)
            }
            R.id.more ->{
                showPopupWindow(Type.ACCOUNT_WINDOW);
            }
            R.id.window_text1 -> dealWithAccount(Type.CHANGE_ACCOUNT)
            R.id.window_text2 -> dealWithAccount(Type.EXIT)
            R.id.window2_text1 -> viewModel.updateEvent(list[updateStatusPos].id)
            R.id.window2_text2 -> viewModel.deleteEvent(list[deletePos].id)
        }
    }

    private fun showPopupWindow(type:Int){
        when(type){
            Type.ACCOUNT_WINDOW -> myWindow.showAtLocation(this.view,Gravity.CENTER,0,0)
            Type.UNFINISHED_EVENT_WINDOW -> myWindow2.showAtLocation(this.view,Gravity.CENTER,0,0)
        }
        val lp = (context as Activity).window.attributes
        lp.alpha = 0.4f; //设置透明度
        (context as Activity).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        (context as Activity).window.attributes = lp;
    }

    private fun dealWithAccount(type:Int){
        when(type){
            Type.EXIT -> activity?.finish()
            Type.CHANGE_ACCOUNT ->{
                var intent = Intent(activity,LoginActivity::class.java)
                startActivityForResult(intent,2)
            }
        }
        if(myWindow.isShowing){
            myWindow.dismiss()
        }
    }

    private fun showEventDetail(){
        var i = Intent(activity,EventDetailActivity::class.java)
        i.putExtra("title",list[updateEventPos].title)
        i.putExtra("content",list[updateEventPos].content)
        i.putExtra("type",list[updateEventPos].type)
        i.putExtra("id",list[updateEventPos].id)
        i.putExtra("status",0)
        startActivityForResult(i,3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && resultCode == 1){
            page = 1
            var pos = myDataBinding.unfinishedListSpinner.selectedItemPosition
            if(pos == 0){
                needClear = true
                viewModel.getInfo(page,0,null,null)
            }else{
                myDataBinding.unfinishedListSpinner.setSelection(0)
            }
        }else if(requestCode == 2 && resultCode==1){
            activity?.finish()
        }else if(requestCode == 3 && resultCode == 1){
            if(data != null){
                list[updateEventPos].title = data.extras.getString("title","")
                list[updateEventPos].content = data.extras.getString("content","")
                list[updateEventPos].type = data.extras.getInt("type")
                adapter.notifyItemChanged(updateEventPos)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        page = 1
        type = position
        needClear = true
        when (position) {
            0 -> viewModel.getInfo(page, 0, null, null)
            1 -> viewModel.getInfo(page, 0, 1, null)
            2 -> viewModel.getInfo(page,0,2,null)
            3 -> viewModel.getInfo(page,0,3,null)
            4 -> viewModel.getInfo(page,0,4,null)
        }
    }
}