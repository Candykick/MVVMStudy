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

private const val TITLE = "title"

class CIdayFragment : Fragment() {
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
        test.add(StockData("12/01/20", 7943, true, 123, 1234))
        test.add(StockData("12/02/20", 7945, true, 10, 98))
        test.add(StockData("12/03/20", 7978, true, 462, 2291))
        test.add(StockData("12/04/20", 7952, false, 300, 1000))
        test.add(StockData("12/07/20", 7921, false, 163, 912))
        test.add(StockData("12/08/20", 8012, true, 1512, 3234))
        test.add(StockData("12/09/20", 7957, false, 735, 1254))
        test.add(StockData("12/10/20", 7990, true, 32, 123))
        test.add(StockData("12/11/20", 7901, false, 3643, 5325))
        test.add(StockData("12/14/20", 7902, true, 8, 11))

        val root = inflater.inflate(R.layout.fragment_c_iday, container, false)

        val rvStock = root.findViewById<RecyclerView>(R.id.rvDayStock)
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

        val btnGotoDeal = root.findViewById<Button>(R.id.btnDayGotoDeal)
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
            CIdayFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, channelTitle)
                }
            }
    }
}