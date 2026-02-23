package com.kwh.almuniconnect.network

import androidx.paging.PagingSource
import androidx.paging.PagingState

class AlumniPagingSource(
    private val repository: AlumniRepository,
    private val ascending: Boolean
) : PagingSource<Int, AlumniDto>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, AlumniDto> {

        return try {
            val page = params.key ?: 1

            val result = repository.getAlumniList(
                pageNumber = page,
                pageSize = 20,
                ascending = ascending
            )

            result.fold(
                onSuccess = { response ->

                    val items = response.items ?: emptyList()

                    LoadResult.Page(
                        data = items,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (items.isEmpty()) null else page + 1
                    )
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, AlumniDto>
    ): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}