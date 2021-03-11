package com.ourstock.ourstock.ui.channel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ourstock.ourstock.R
import com.ourstock.ourstock.ui.order.OrderActivity
import kr.co.prnd.YouTubePlayerView
import java.text.DecimalFormat

private const val TITLE = "title"

class ChannelInfoFragment : Fragment() {
    val numFormat = DecimalFormat("###,###")

    private lateinit var channelTitle: String

    val test = ChannelDeepInfo(
        74034, 1.59, 79353, 4.13, 73205, 0.94,
        14234, 6200, 15, "우수몽", 215, 853432756,
        436, "2021/02/02", "382BTxLNrow"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            channelTitle = it.getString(TITLE)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_channel_info, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val tvChannelInfoValue = view?.findViewById<TextView>(R.id.tvChannelInfoValue)
        val tvChannelInfoVPercent = view?.findViewById<TextView>(R.id.tvChannelInfoVPercent)
        val tvChannelInfoHighValue = view?.findViewById<TextView>(R.id.tvChannelInfoHighValue)
        val tvChannelInfoVHPercent = view?.findViewById<TextView>(R.id.tvChannelInfoVHPercent)
        val tvChannelInfoLowValue = view?.findViewById<TextView>(R.id.tvChannelInfoLowValue)
        val tvChannelInfoVLPercent = view?.findViewById<TextView>(R.id.tvChannelInfoVLPercent)
        val tvChannelInfoVolume = view?.findViewById<TextView>(R.id.tvChannelInfoVolume)
        val tvChannelInfoTradeCost = view?.findViewById<TextView>(R.id.tvChannelInfoTradeCost)
        val tvChannelInfoCapitalization =
            view?.findViewById<TextView>(R.id.tvChannelInfoCapitalization)
        val tvChannelInfoShareholder = view?.findViewById<TextView>(R.id.tvChannelInfoShareholder)
        val tvChannelInfoSubscriberNum =
            view?.findViewById<TextView>(R.id.tvChannelInfoSubscriberNum)
        val tvChannelInfoSumHits = view?.findViewById<TextView>(R.id.tvChannelInfoSumHits)
        val tvChannelInfoSumVideos = view?.findViewById<TextView>(R.id.tvChannelInfoSumVideos)
        val tvChannelInfoDate = view?.findViewById<TextView>(R.id.tvChannelInfoDate)
        val btnGotoDeal = view?.findViewById<Button>(R.id.btnCIGoToDeal)

        tvChannelInfoValue?.setText(numFormat.format(test.value))
        tvChannelInfoVPercent?.setText("- "+test.valuePercent+"%")
        tvChannelInfoHighValue?.setText(numFormat.format(test.high))
        tvChannelInfoVHPercent?.setText("+ "+test.highPercent+"%")
        tvChannelInfoLowValue?.setText(numFormat.format(test.low))
        tvChannelInfoVLPercent?.setText("- "+test.lowPercent+"%")
        tvChannelInfoVolume?.setText(numFormat.format(test.volume))
        tvChannelInfoTradeCost?.setText(numFormat.format(test.tradeCost)+"만")
        tvChannelInfoCapitalization?.setText(numFormat.format(test.capitalization)+"억")
        tvChannelInfoShareholder?.setText(test.shareholder)
        tvChannelInfoSubscriberNum?.setText(numFormat.format(test.subscriberNum)+"만명")
        tvChannelInfoSumHits?.setText(numFormat.format(test.sumHits)+"회")
        tvChannelInfoSumVideos?.setText(numFormat.format(test.videoNum)+"개")
        tvChannelInfoDate?.setText(test.date)

        btnGotoDeal?.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            intent.putExtra("title", channelTitle)
            startActivity(intent)
        }

        val yvChannelInfo = view?.findViewById<YouTubePlayerView>(R.id.yvChannelInfo)
        yvChannelInfo?.play(test.videoUrl)
    }

    companion object {
        @JvmStatic
        fun newInstance(channelTitle: String) =
            ChannelInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, channelTitle)
                }
            }
    }
}

data class ChannelDeepInfo(val value: Int, val valuePercent: Double, val high: Int, val highPercent: Double,
                       val low: Int, val lowPercent: Double, val volume: Int, val tradeCost: Int, val capitalization: Int,
                       val shareholder: String, val subscriberNum: Int, val sumHits: Int, val videoNum: Int, val date: String,
                       val videoUrl: String)