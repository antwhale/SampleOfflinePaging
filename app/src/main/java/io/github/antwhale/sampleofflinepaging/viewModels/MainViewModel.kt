package io.github.antwhale.sampleofflinepaging.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import io.github.antwhale.sampleofflinepaging.api.GithubService
import io.github.antwhale.sampleofflinepaging.db.Repo
import io.github.antwhale.sampleofflinepaging.db.RepoDatabase
import io.github.antwhale.sampleofflinepaging.model.UiState
import io.github.antwhale.sampleofflinepaging.repositories.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = MainViewModel::class.java.simpleName

    private var repository : GithubRepository?
    private var repoDatabase : RepoDatabase?

    private val _recyclerViewState : MutableStateFlow<UiState<PagingData<Repo>>> = MutableStateFlow(UiState.Loading)
    val recyclerViewState : StateFlow<UiState<PagingData<Repo>>> = _recyclerViewState

    init {
        Log.d(TAG, "MainViewModel init")
        val githubService = GithubService.create()
        repoDatabase = RepoDatabase.getInstance(application.applicationContext)
        repository = GithubRepository(githubService, repoDatabase!!)
    }

    //paging with RemoteMediator
    fun getGithubInfo() =
        repository?.getGithubInfo()
        ?.onEach { pagingData ->
            Log.d(TAG, "pagingData : $pagingData, Thread: ${Thread.currentThread().name}")
            _recyclerViewState.value = UiState.Success(pagingData)

        }
        ?.catch { error ->
            error.printStackTrace()
            _recyclerViewState.value = UiState.Error
        }
        ?.flowOn(Dispatchers.IO)
        ?.launchIn(viewModelScope)


    //paging without RemoteMediator
    suspend fun getGithubInfo2() = repository?.getGithubInfo2()
        ?.onEach {pagingData ->
            Log.d(TAG, "pagingData : $pagingData")
            _recyclerViewState.value = UiState.Success(pagingData)
        }?.catch{e ->
            e.printStackTrace()
            _recyclerViewState.value = UiState.Error
        }?.flowOn(Dispatchers.IO)?.launchIn(viewModelScope)

//    suspend fun testFunction() {
//        val response = GithubService.create().searchRepos("abc", 1, 20)
//        Log.d(TAG, "testFunction: ${response.items}")
//    }
}