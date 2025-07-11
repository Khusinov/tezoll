package uz.khusinov.karvon.domain.use_case.auth

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val logoutUseCase: LogoutUseCase,
    val confirmUseCase: ConfirmUseCase
)
