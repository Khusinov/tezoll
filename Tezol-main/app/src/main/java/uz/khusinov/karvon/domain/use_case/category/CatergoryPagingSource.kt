package uz.khusinov.karvon.domain.use_case.category

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.shop.CategoryRespons

class CatergoryPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, CategoryRespons>() {

    // Paging uchun refresh kalitini olish
    override fun getRefreshKey(state: PagingState<Int, CategoryRespons>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryRespons> {
        return try {
            val nextPage = params.key ?: 1

            // API chaqiruvni amalga oshirish
            val response = apiService.getCategories(page = nextPage)

            // Agar response null bo'lsa, LoadResult.Error qaytarish
            if (response == null || response.data.results.isEmpty()) {
                return LoadResult.Error(Exception("Bo'sh yoki noto'g'ri response"))
            }

            // Keyingi sahifani aniqlash
            val nextKey = if (response.data.next == null) {
                null // Sahifalash tugadi
            } else {
                nextPage + 1
            }

            // LoadResult.Page orqali ma'lumotlarni qaytarish
            LoadResult.Page(
                data = response.data.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextKey
            )

        } catch (e: Exception) {
            Log.e("ContactsPagingSource", "load: ${e.message}")
            LoadResult.Error(e)
        }
    }
}