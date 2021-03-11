package com.ourstock.ourstock.ui.channel

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.ourstocktest.BaseActivity
import com.example.ourstocktest.HomeViewModel
import com.example.ourstocktest.R
import com.example.ourstocktest.YoutuberListAdapter
import com.example.ourstocktest.channel.ChannelInfoViewModel
import com.example.ourstocktest.databinding.ActivityChannelInfoBinding
import com.example.ourstocktest.databinding.ActivityHomeBinding
import com.example.ourstocktest.dialog.LoadingDialog
import com.example.ourstocktest.model.ChannelData
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_channel_info.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.DecimalFormat

class ChannelInfoActivity : BaseActivity() {
    val numFormat = DecimalFormat("###,###")

    // Loading Dialog 및 MVVM 관련 객체들
    val loadingDialog: LoadingDialog by inject { parametersOf(this@ChannelInfoActivity) }
    private val binding by binding<ActivityChannelInfoBinding>(R.layout.activity_channel_info)
    private val viewModel: ChannelInfoViewModel by viewModel { parametersOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore instance state
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ChannelInfoActivity
            viewModel = this@ChannelInfoActivity.viewModel
        }

        // 테스트용 데이터 (이전 화면에서 intent로 받아옴)
        val channelMain = ChannelData(
            intent.getStringExtra("image")!!,
            intent.getStringExtra("title")!!,
            intent.getIntExtra("stock", 0),
            intent.getBooleanExtra("isUp", true),
            intent.getIntExtra("variation", 0),
            intent.getDoubleExtra("percent", 0.0)
        )

        // 뒤로가기 버튼
        btnChannelInfoBack.setOnClickListener {
            finish()
        }

        // 좋아요 버튼
        btnChannelInfoLike.setOnClickListener {

        }

        // 채널 Main 정보 셋팅
        Glide.with(this@ChannelInfoActivity).load(channelMain.image).into(ivChannelInfo)
        tvChannelInfoTitle.setText(channelMain.title)
        tvChannelInfoStock.setText(numFormat.format(channelMain.stock))
        if(channelMain.isUp) {
            tvChannelInfoStock.setTextColor(ContextCompat.getColor(this@ChannelInfoActivity, R.color.stockUp))
            tvChannelInfoVariation.setTextColor(ContextCompat.getColor(this@ChannelInfoActivity, R.color.stockUp))
            tvChannelInfoVariation.setText(
                "▲ " + channelMain.variation + " (+ " + channelMain.percent + "%)")
        } else {
            tvChannelInfoStock.setTextColor(ContextCompat.getColor(this@ChannelInfoActivity, R.color.stockDown))
            tvChannelInfoVariation.setTextColor(ContextCompat.getColor(this@ChannelInfoActivity, R.color.stockDown))
            tvChannelInfoVariation.setText(
                "▼ " + channelMain.variation + " (- " + channelMain.percent + "%)")
        }

        // 정보, 실시간, 일별 탭
        val vpAdapter = TabAdapter(this@ChannelInfoActivity)
        vpChannelInfo.adapter = vpAdapter
        vpChannelInfo.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(tabChannelInfo, vpChannelInfo) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.channel_info_tab1)
                1 -> tab.text = getString(R.string.channel_info_tab2)
                2 -> tab.text = getString(R.string.channel_info_tab3)
            }
        }.attach()
    }

    inner class TabAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0       ->  ChannelInfoFragment.newInstance(intent.getStringExtra("title")!!)
                1       ->  CIRealTimeFragment.newInstance(intent.getStringExtra("title")!!)
                2       ->  CIdayFragment.newInstance(intent.getStringExtra("title")!!)
                else -> ChannelInfoFragment.newInstance(intent.getStringExtra("title")!!)
            }
        }
    }
}