package io.github.antwhale.sampleofflinepaging.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.antwhale.sampleofflinepaging.R
import io.github.antwhale.sampleofflinepaging.adapters.ReposAdapter
import io.github.antwhale.sampleofflinepaging.databinding.ActivityMainBinding
import io.github.antwhale.sampleofflinepaging.model.UiState
import io.github.antwhale.sampleofflinepaging.viewModels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()
    private val reposAdapter : ReposAdapter by lazy { ReposAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        activityInit()
        observeInit()
        dataInit()
    }

    private fun activityInit() {
        Log.d(TAG, "activityInit")

        binding.recyclerView.adapter = reposAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)
        val decoration = DividerItemDecoration(baseContext, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
    }

    private fun dataInit() {
        Log.d(TAG, "dataInit")
        mainViewModel.getGithubInfo()
    }

    private fun observeInit() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.recyclerViewState.collect { uiState ->
                    when(uiState) {
                        is UiState.Error -> {
                            Log.d(TAG, "uiState is Error")
                        }
                        is UiState.Success -> {
                            Log.d(TAG, "uiState is Success")
                            reposAdapter.submitData(uiState.result)
                        }
                        is UiState.Loading -> {
                            Log.d(TAG, "uiState is Loading")

                        }
                    }
                }
            }
        }
    }
}