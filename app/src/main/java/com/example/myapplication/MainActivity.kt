package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.skydoves.powerspinner.IconSpinnerAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var items = mutableListOf<DustItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.spinnerViewSido.setOnSpinnerItemSelectedListener<String> {_, _, _, text ->
            // 시도 선택시 구정보를 spinniViewGoo에 초기화
            communicateNetWork(setUpDustParameter(text))
        }

        binding.spinnerViewGoo.setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
            // items에는 선택한 시도의 구정보가 들어있는상태
            var selectedItem = items.filter { it.stationName == text}

            // 실시간정보 이므로 가장 최신정보인 0번째 인덱스를 받아옴
            binding.tvCityname.text = "${selectedItem[0].sidoName} ${selectedItem[0].stationName}"
            binding.tvDate.text = selectedItem[0].dataTime
            binding.tvP10value.text = selectedItem[0].pm10Value + " ㎍/㎥"
        }

    }

    // 인터넷 통신을 위해 별도의 코루틴 사용
    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch() {
        // HashMap을 받아 Dust 데이터클래스 형태로 리턴 받음
        val responseData = NetWorkClient.dustNetWork.getDust(param)
        val adapter = IconSpinnerAdapter(binding.spinnerViewGoo)

        //items = responseData.response.dustBody.dustItems

        val goo = ArrayList<String>()
        items.forEach {
            goo.add(it.stationName)
        }

        // 시도마다 구정보가 다르므로 별로의 코루틴에서 실행
        runOnUiThread{
            binding.spinnerViewGoo.setItems(goo)
        }
    }

    // 인증키와 시도명으로 HashMap을 리턴받는 메서드
    private fun setUpDustParameter(sido: String): HashMap<String, String> {
        val authKey =
            "nGc+cK6XiQOe6N7uNc1UGrtyWqGxmKovaZ+Jd8dDfyXaFa0X+H+ItF4/R/bcAQKjtyZPM8IblvzilLfgB247tg=="

        return hashMapOf(
            "serviceKey" to authKey,
            "returnType" to "json",
            "numOfRows" to "100",
            "pageNo" to "1",
            "sidoName" to sido,
            "ver" to "1.0"
        )
    }
}
