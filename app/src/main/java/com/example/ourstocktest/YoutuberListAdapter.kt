package com.example.ourstocktest

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ourstocktest.model.ChannelData
import com.ourstock.ourstock.ui.channel.ChannelInfoActivity
import java.text.DecimalFormat

class YoutuberListAdapter(private val context: Context) : RecyclerView.Adapter<YoutuberListAdapter.Holder>() {
    var listData: ArrayList<ChannelData> = ArrayList()
    val numFormat = DecimalFormat("###,###")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_recycler_main, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.ivMain1).load(listData[position*2].image).into(holder.ivMain1)
        holder.tvTitle1.setText(listData[position*2].title)
        holder.tvStock1.setText(numFormat.format(listData[position*2].stock))

        if(listData[position*2].isUp) {
            holder.tvStock1.setTextColor(ContextCompat.getColor(context, R.color.stockUp))
            holder.tvVariation1.setTextColor(ContextCompat.getColor(context, R.color.stockUp))
            holder.tvVariation1.setText("▲ " + listData[position*2].variation + " (+ " + listData[position*2].percent + "%)")
        } else {
            holder.tvStock1.setTextColor(ContextCompat.getColor(context, R.color.stockDown))
            holder.tvVariation1.setTextColor(ContextCompat.getColor(context, R.color.stockDown))
            holder.tvVariation1.setText("▼ " + listData[position*2].variation + " (- " + listData[position*2].percent + "%)")
        }

        holder.cell1.setOnClickListener {
            val intent = Intent(context, ChannelInfoActivity::class.java)
            intent.putExtra("image", listData[position*2].image)
            intent.putExtra("title", listData[position*2].title)
            intent.putExtra("stock", listData[position*2].stock)
            intent.putExtra("isUp", listData[position*2].isUp)
            intent.putExtra("variation", listData[position*2].variation)
            intent.putExtra("percent", listData[position*2].percent)
            context.startActivity(intent)
        }

        if(position*2+1 < listData.size) {
            Glide.with(holder.ivMain2).load(listData[position * 2 + 1].image).into(holder.ivMain2)
            holder.tvTitle2.setText(listData[position * 2 + 1].title)
            holder.tvStock2.setText(numFormat.format(listData[position * 2 + 1].stock))

            if (listData[position * 2 + 1].isUp) {
                holder.tvStock2.setTextColor(ContextCompat.getColor(context, R.color.stockUp))
                holder.tvVariation2.setTextColor(ContextCompat.getColor(context, R.color.stockUp))
                holder.tvVariation2.setText("▲ " + listData[position * 2 + 1].variation + " (+ " + listData[position * 2 + 1].percent + "%)")
            } else {
                holder.tvStock2.setTextColor(ContextCompat.getColor(context, R.color.stockDown))
                holder.tvVariation2.setTextColor(ContextCompat.getColor(context, R.color.stockDown))
                holder.tvVariation2.setText("▼ " + listData[position * 2 + 1].variation + " (- " + listData[position * 2 + 1].percent + "%)")
            }

            holder.cell2.setOnClickListener {
                val intent = Intent(context, ChannelInfoActivity::class.java)
                intent.putExtra("image", listData[position*2+1].image)
                intent.putExtra("title", listData[position*2+1].title)
                intent.putExtra("stock", listData[position*2+1].stock)
                intent.putExtra("isUp", listData[position*2+1].isUp)
                intent.putExtra("variation", listData[position*2+1].variation)
                intent.putExtra("percent", listData[position*2+1].percent)
                context.startActivity(intent)
            }
        } else {
            holder.cell2.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return if(listData.size%2 == 0)
            listData.size/2
        else
            listData.size/2+1
    }

    fun setData(newData: ArrayList<ChannelData>) {
        listData = newData
        notifyDataSetChanged()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cell1: ConstraintLayout = itemView.findViewById(R.id.rowMain1)
        val ivMain1: ImageView = itemView.findViewById(R.id.rowMainImage1)
        val tvTitle1: TextView = itemView.findViewById(R.id.rowMainTitle1)
        val tvStock1: TextView = itemView.findViewById(R.id.rowMainStock1)
        val tvVariation1: TextView = itemView.findViewById(R.id.rowMainVariation1)

        val cell2: ConstraintLayout = itemView.findViewById(R.id.rowMain2)
        val ivMain2: ImageView = itemView.findViewById(R.id.rowMainImage2)
        val tvTitle2: TextView = itemView.findViewById(R.id.rowMainTitle2)
        val tvStock2: TextView = itemView.findViewById(R.id.rowMainStock2)
        val tvVariation2: TextView = itemView.findViewById(R.id.rowMainVariation2)
    }
}