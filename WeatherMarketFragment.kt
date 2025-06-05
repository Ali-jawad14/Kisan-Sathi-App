package com.example.kisansathi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisansathi.adapters.MarketPriceAdapter
import com.example.kisansathi.adapters.WeatherForecastAdapter
import com.example.kisansathi.databinding.FragmentWeatherMarketBinding
import com.example.kisansathi.models.MarketPrice
import com.example.kisansathi.models.WeatherData
import com.example.kisansathi.network.WeatherRepository
import com.example.kisansathi.network.MarketRepository
import kotlinx.coroutines.launch

class WeatherMarketFragment : Fragment() {

    private var _binding: FragmentWeatherMarketBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var weatherAdapter: WeatherForecastAdapter
    private lateinit var marketAdapter: MarketPriceAdapter
    private val weatherRepository = WeatherRepository()
    private val marketRepository = MarketRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherMarketBinding.inflate(inflater, container, false)
        
        setupRecyclerViews()
        loadData()
        
        return binding.root
    }

    private fun setupRecyclerViews() {
        weatherAdapter = WeatherForecastAdapter()
        binding.recyclerWeather.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerWeather.adapter = weatherAdapter
        
        marketAdapter = MarketPriceAdapter()
        binding.recyclerMarket.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMarket.adapter = marketAdapter
    }

    private fun loadData() {
        loadWeatherData()
        loadMarketData()
    }

    private fun loadWeatherData() {
        lifecycleScope.launch {
            try {
                binding.progressWeather.visibility = View.VISIBLE
                val weatherData = weatherRepository.getCurrentWeather("Lahore") // Default city
                val forecast = weatherRepository.getWeatherForecast("Lahore")
                
                updateCurrentWeather(weatherData)
                weatherAdapter.updateForecast(forecast)
                
            } catch (e: Exception) {
                binding.textWeatherError.visibility = View.VISIBLE
                binding.textWeatherError.text = "موسمی ڈیٹا لوڈ نہیں ہو سکا"
            } finally {
                binding.progressWeather.visibility = View.GONE
            }
        }
    }

    private fun loadMarketData() {
        lifecycleScope.launch {
            try {
                binding.progressMarket.visibility = View.VISIBLE
                val marketPrices = marketRepository.getMarketPrices()
                marketAdapter.updatePrices(marketPrices)
                
            } catch (e: Exception) {
                binding.textMarketError.visibility = View.VISIBLE
                binding.textMarketError.text = "مارکیٹ کی قیمتیں لوڈ نہیں ہو سکیں"
            } finally {
                binding.progressMarket.visibility = View.GONE
            }
        }
    }

    private fun updateCurrentWeather(weather: WeatherData) {
        binding.apply {
            textTemperature.text = "${weather.temperature}°C"
            textWeatherDescription.text = weather.description
            textHumidity.text = "نمی: ${weather.humidity}%"
            textWindSpeed.text = "ہوا: ${weather.windSpeed} km/h"
            
            // Weather advice for farmers
            textWeatherAdvice.text = getWeatherAdvice(weather)
        }
    }

    private fun getWeatherAdvice(weather: WeatherData): String {
        return when {
            weather.temperature > 35 -> "بہت گرم موسم - فصلوں کو زیادہ پانی دیں"
            weather.temperature < 10 -> "ٹھنڈا موسم - فصلوں کو ٹھنڈ سے بچائیں"
            weather.humidity > 80 -> "زیادہ نمی - فنگل بیماریوں سے بچاؤ کریں"
            weather.windSpeed > 20 -> "تیز ہوا - کمزور پودوں کو سہارا دیں"
            else -> "موسم کاشت کے لیے موزوں ہے"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
