package com.ourstock.ourstock.ui.channel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ourstock.ourstock.R
import com.ourstock.ourstock.ui.adapter.StockChartAdapter
import com.ourstock.ourstock.ui.adapter.StockData
import com.ourstock.ourstock.ui.order.OrderActivity

/** 문제점
 * 맨 아래 차트가 안 보인다. 버튼 위까지 올라올 수 있도록 해야 할 듯.
 * 근데 애초에 이렇게 차트가 가려지는 게 맞는거야? 아닌거같지?
 */

private const val TITLE = "title"

class CIRealTimeFragment : Fragment() {
    private lateinit var channelTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            channelTitle = it.getString(TITLE)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 테스트용 차트
        var test: ArrayList<StockData> = ArrayList()
        test.add(StockData("17:01:19", 7943, true, 123, 1234))
        test.add(StockData("17:01:25", 7945, true, 10, 98))
        test.add(StockData("17:01:27", 7978, true, 462, 2291))
        test.add(StockData("17:01:30", 7952, false, 300, 1000))
        test.add(StockData("17:01:35", 7921, false, 163, 912))
        test.add(StockData("17:01:59", 8012, true, 1512, 3234))
        test.add(StockData("17:02:35", 7957, false, 735, 1254))
        test.add(StockData("17:03:05", 7990, true, 32, 123))
        test.add(StockData("17:05:17", 7901, false, 3643, 5325))
        test.add(StockData("17:07:42", 7902, true, 8, 11))

        val root = inflater.inflate(R.layout.fragment_c_i_real_time, container, false)

        val rvStock = root.findViewById<RecyclerView>(R.id.rvRealTimeStock)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvStock.layoutManager = linearLayoutManager
        rvStock.adapter = StockChartAdapter(context!!, test)

        rvStock.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 최상단 이벤트 감지 : 전체 Refresh
                if(!recyclerView.canScrollVertically(-1)) {

                }
                // 최하단 이벤트 감지 : 추가로 몇십개의 데이터 갱신
                else if(!recyclerView.canScrollVertically(1)) {

                }
            }
        })

        val btnGotoDeal = root.findViewById<Button>(R.id.btnRealTimeGotoDeal)
        btnGotoDeal.setOnClickListener {
            val intent = Intent(activity, OrderActivity::class.java)
            intent.putExtra("title", channelTitle)
            startActivity(intent)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(channelTitle: String) =
            CIRealTimeFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, channelTitle)
                }
            }
    }
}