/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.antwhale.sampleofflinepaging.repositories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.antwhale.sampleofflinepaging.api.GithubService
import io.github.antwhale.sampleofflinepaging.db.GithubRemoteMediator
import io.github.antwhale.sampleofflinepaging.db.PAGE_SIZE
import io.github.antwhale.sampleofflinepaging.db.Repo
import io.github.antwhale.sampleofflinepaging.db.RepoDatabase
import io.github.antwhale.sampleofflinepaging.db.RepoPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(private val service: GithubService, private val database: RepoDatabase) {

    val TAG = GithubRepository::class.java.simpleName
    val repoDao = database.reposDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getGithubInfo() = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        remoteMediator = GithubRemoteMediator(
            "abc",
            service,
            database
        )
    ) {
        database.reposDao().pagingSource()
    }.flow

    suspend fun getGithubInfo2() = withContext(Dispatchers.IO) {
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { RepoPagingSource(service) }
        ).flow.also {
            Log.d(TAG, "getGithubInfo2")
        }
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 30
        const val IN_QUALIFIER = "in:name,description"
    }
}
