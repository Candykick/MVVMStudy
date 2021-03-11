package com.example.ourstocktest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ourstocktest.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivity : BaseActivity() {

    private val adapter = YoutuberListAdapter(this@HomeActivity)
    // Loading Dialog 및 MVVM 관련 객체들
    private val binding by binding<ActivityHomeBinding>(R.layout.activity_home)
    private val viewModel: HomeViewModel by viewModel { parametersOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore instance state
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@HomeActivity
            viewModel = this@HomeActivity.viewModel
        }

        /** 채널 리스트 RecyclerView 관련 설정 */
        val linearLayoutManager = LinearLayoutManager(this@HomeActivity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvHome.layoutManager = linearLayoutManager
        rvHome.adapter = adapter

        rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 최하단 이벤트 감지 : 추가로 몇십개의 데이터 갱신
                if(!recyclerView.canScrollVertically(1)) {

                }
            }
        })

        /** 최상단 이벤트 감지 : 전체 Refresh (SwipeRefreshLayout 이용) */
        srlHome.setOnRefreshListener {
            // 채널 RecyclerView를 새로고침 -> 데이터 로딩이 끝나면 아래의 channelList의 observe로 이동.
            viewModel.refreshChannelList()
        }

        /** ViewModel 갱신 시 처리 */
        viewModel.channelList.observe(binding.lifecycleOwner!!, Observer {
            if(viewModel.channelList.value != null) {
                adapter.setData(viewModel.channelList.value!!)
                // 새로고침 완료 : 새로고침 아이콘을 없앰.
                srlHome.isRefreshing = false
            }
        })

        viewModel.error.observe(binding.lifecycleOwner!!, Observer {
            if(viewModel.error.value != null) {
                ivHomeLoadFailed.visibility = View.VISIBLE
                rvHome.visibility = View.GONE

                Toast.makeText(this@HomeActivity, viewModel.error.value!!.message, Toast.LENGTH_LONG).show()
                srlHome.isRefreshing = false
            } else {
                ivHomeLoadFailed.visibility = View.GONE
                rvHome.visibility = View.VISIBLE
                srlHome.isRefreshing = false
            }
        })
    }
}