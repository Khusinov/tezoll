package uz.khusinov.karvon.domain.use_case.profile

data class ProfileUseCases(
    val getMeUseCase: GetMeUseCase,
    val getCardListUseCase: GetCardListUseCase,
    val addCardUseCase: AddCardUseCase,
    val verifyCardUseCase: VerifyCardUseCase,
    val removeCardUseCase: RemoveCardUseCase,
    val setMainCardUseCase: SetMainCardUseCase
)
