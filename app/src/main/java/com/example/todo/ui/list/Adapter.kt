package com.example.todo.ui.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.Datas
import com.example.todo.data.model.Event
import com.example.todo.databinding.EventListItemBinding
import com.example.todo.utils.Type
import java.util.zip.Inflater


class Adapter(list:ArrayList<Datas>): RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private val myList = list;
    private lateinit var context : Context
    private lateinit var listener:OnItemClickListener
    private lateinit var viewListener:OnViewClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        var binding: EventListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.event_list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return myList.size;
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.event = myList[position]
        when{
            myList[position].type == Type.LIFE -> {
                holder.binding.eventTag.background = ContextCompat.getDrawable(context,R.drawable.tag_life)
                holder.binding.eventTag.text = context.getString(R.string.life)
            }
            myList[position].type == Type.WORK -> {
                holder.binding.eventTag.background = ContextCompat.getDrawable(context,R.drawable.tag_work)
                holder.binding.eventTag.text = context.getString(R.string.work)
            }
            myList[position].type == Type.LEARN -> {
                holder.binding.eventTag.background = ContextCompat.getDrawable(context,R.drawable.tag_learn)
                holder.binding.eventTag.text = context.getString(R.string.learn)
            }
            myList[position].type == Type.OTHER -> {
                holder.binding.eventTag.background = ContextCompat.getDrawable(context,R.drawable.tag_other)
                holder.binding.eventTag.text =context.getString(R.string.other)
            }
        }
        holder.binding.listItemImage.setOnClickListener {
            listener.onItemClick(holder.adapterPosition);
        }
        holder.binding.root.setOnClickListener{
            viewListener.onItemClick(holder.adapterPosition)
        }
    }

    inner class MyViewHolder(var binding: EventListItemBinding): RecyclerView.ViewHolder(binding.root) {
    }

    fun setListener(listener:OnItemClickListener){
        this.listener = listener
    }

    fun setViewListener(viewListener: OnViewClickListener){
        this.viewListener = viewListener
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    interface OnViewClickListener{
        fun onItemClick(position: Int)
    }
}