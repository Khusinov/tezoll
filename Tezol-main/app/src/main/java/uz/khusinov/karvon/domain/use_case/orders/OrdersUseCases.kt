package uz.khusinov.karvon.domain.use_case.orders


data class OrdersUseCases(
    val getOrdersUseCase: GetOrdersUseCase,
    val getOrdersHistoryUseCase: GetOrdersHistoryUseCase
)