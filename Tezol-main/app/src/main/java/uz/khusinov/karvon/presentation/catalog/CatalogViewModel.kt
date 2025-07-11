package uz.khusinov.karvon.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.domain.use_case.category.CategoryUseCases
import uz.khusinov.karvon.domain.use_case.category.CatergoryPagingSource
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val apiService: ApiService
) : ViewModel() {

    val categoryPaging: Flow<PagingData<CategoryRespons>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CatergoryPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)

}