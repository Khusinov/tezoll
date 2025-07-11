package uz.khusinov.karvon.domain.use_case.home

import kotlinx.coroutines.flow.Flow
import uz.khusinov.karvon.domain.model.AdsResponse
import uz.khusinov.karvon.domain.repository.HomeRepository
import uz.khusinov.karvon.utills.UiStateObject

class GetAdsUseCase(
    private val repository: HomeRepository
) {

    operator fun invoke(): Flow<UiStateObject<AdsResponse>> {
        return repository.getAds()
    }
}