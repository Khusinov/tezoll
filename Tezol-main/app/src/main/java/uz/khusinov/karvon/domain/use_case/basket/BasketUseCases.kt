package uz.khusinov.karvon.domain.use_case.basket

data class BasketUseCases(
    val basketUseCase: BasketUseCase,
    val addressUseCase: AddressUseCase,
    val getDeliveryPriceUseCase: GetDeliveryPriceUseCase
)
