package com.example.ourstocktest.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.ourstocktest.R
import kotlinx.android.synthetic.main.bottom_dialog_telecom.*


class TelecomBottomDialog(context: Context, telecomList: Array<String>) : BottomSheetDialogFragment(), DialogCallback {
    lateinit var dialogListener: BottomDialogListener
    val telecomList = telecomList
    val dialogContext = context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottom_dialog_telecom, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvTelecom.adapter = TelecomRecyclerAdapter(dialogContext, telecomList)
        rvTelecom.layoutManager = LinearLayoutManager(activity)

        btnCloseDTelecom.setOnClickListener {
            dismiss()
        }
    }

    override fun setTelecom(position: Int) {
        dismiss()
        dialogListener.onClicked(telecomList[position])
        //return telecomList[position]
    }

    interface BottomDialogListener {
        fun onClicked(data: String)
    }

    inner class TelecomRecyclerAdapter(private val context: Context, private val telecomList: Array<String>) : RecyclerView.Adapter<TelecomRecyclerAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelecomRecyclerAdapter.Holder {
            val view = LayoutInflater.from(context).inflate(R.layout.row_recycler_telecom, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.tvTitle.text = telecomList[position]

            holder.rowTelecom.setOnClickListener {
                setTelecom(position)
                dismiss()
            }
        }

        override fun getItemCount(): Int {
            return telecomList.size
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val rowTelecom: FrameLayout = itemView.findViewById(R.id.rowTelecom)
            val tvTitle: TextView = itemView.findViewById(R.id.rowTelecomTitle)
        }
    }
}

interface DialogCallback {
    fun setTelecom(position: Int)
}