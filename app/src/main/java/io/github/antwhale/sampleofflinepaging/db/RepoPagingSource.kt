package io.github.antwhale.sampleofflinepaging.db

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.github.antwhale.sampleofflinepaging.api.GithubService

const val START_PAGE = 1
const val PAGE_SIZE = 20
class RepoPagingSource(val githubService: GithubService) : PagingSource<Int, Repo>(){
    val TAG = "RepoPagingSource"

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        Log.d(TAG, "getRefreshKey")
        val anchorPosition = state.anchorPosition ?: return null
        val repo = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = repo.id.toInt() - (state.config.pageSize / 2))    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        Log.d(TAG, "load: ")
        val start = params.key ?: START_PAGE

        val response = githubService.searchRepos("abc", start, PAGE_SIZE)
        val repos = response.items

        Log.d(TAG, "start : $start, repos: $repos")

        return LoadResult.Page(
            data = repos,
            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = if(start == START_PAGE) null else start - 1,
            nextKey = if(repos.isEmpty()) null else start + 1
        )
    }

    private fun ensureValidKey(key: Int) = START_PAGE.coerceAtLeast(key)

}