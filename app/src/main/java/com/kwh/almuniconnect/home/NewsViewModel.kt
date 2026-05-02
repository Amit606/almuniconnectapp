package com.kwh.almuniconnect.home
import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow

class NewsViewModel : ViewModel() {

    private val _state = MutableStateFlow(NewsState1(isLoading = true))
    val state: MutableStateFlow<NewsState1> = _state

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        setupRemoteConfig()
        loadNews()
    }

    private fun setupRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // testing only
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun loadNews() {
        _state.value = NewsState1(isLoading = true)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    _state.value = NewsState1(error = "Fetch failed")
                    return@addOnCompleteListener
                }

                val json = remoteConfig.getString("news_json")

                if (json.isNullOrEmpty()) {
                    _state.value = NewsState1(error = "Empty JSON")
                    return@addOnCompleteListener
                }

                try {
                    val type = object : TypeToken<List<UniversityNews>>() {}.type
                    val newsList: List<UniversityNews> = Gson().fromJson(json, type)

                    _state.value = NewsState1(news = newsList)

                } catch (e: Exception) {
                    _state.value = NewsState1(error = e.message ?: "Parsing error")
                }
            }
    }
}

data class NewsState1(
    val isLoading: Boolean = false,
    val news: List<UniversityNews> = emptyList(),
    val error: String? = null
)