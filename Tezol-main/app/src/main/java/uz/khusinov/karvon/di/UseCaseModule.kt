package uz.khusinov.karvon.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.khusinov.karvon.domain.repository.AuthRepository
import uz.khusinov.karvon.domain.repository.BasketRepository
import uz.khusinov.karvon.domain.repository.CategoryRepository
import uz.khusinov.karvon.domain.repository.HomeRepository
import uz.khusinov.karvon.domain.repository.OrdersRepository
import uz.khusinov.karvon.domain.repository.ProductsRepository
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.domain.repository.ShopsRepository
import uz.khusinov.karvon.domain.use_case.auth.AuthUseCases
import uz.khusinov.karvon.domain.use_case.auth.ConfirmUseCase
import uz.khusinov.karvon.domain.use_case.auth.LoginUseCase
import uz.khusinov.karvon.domain.use_case.auth.LogoutUseCase
import uz.khusinov.karvon.domain.use_case.basket.AddressUseCase
import uz.khusinov.karvon.domain.use_case.basket.BasketUseCase
import uz.khusinov.karvon.domain.use_case.category.CategoryUseCases
import uz.khusinov.karvon.domain.use_case.category.GetCategoryUseCase
import uz.khusinov.karvon.domain.use_case.home.GetAdsUseCase
import uz.khusinov.karvon.domain.use_case.home.GetMostSellProductsUseCase
import uz.khusinov.karvon.domain.use_case.home.GetTopProductsUseCase
import uz.khusinov.karvon.domain.use_case.home.HomeUseCases
import uz.khusinov.karvon.domain.use_case.home.SearchProductsUseCase
import uz.khusinov.karvon.domain.use_case.orders.GetOrdersHistoryUseCase
import uz.khusinov.karvon.domain.use_case.orders.GetOrdersUseCase
import uz.khusinov.karvon.domain.use_case.orders.OrdersUseCases
import uz.khusinov.karvon.domain.use_case.products.DeleteAllProductUseCase
import uz.khusinov.karvon.domain.use_case.products.InsertProductUseCase
import uz.khusinov.karvon.domain.use_case.products.ProductsUseCases
import uz.khusinov.karvon.domain.use_case.products.RemoveProductUseCase
import uz.khusinov.karvon.domain.use_case.products.UpdateProductUseCase
import uz.khusinov.karvon.domain.use_case.profile.GetMeUseCase
import uz.khusinov.karvon.domain.use_case.profile.ProfileUseCases
import uz.khusinov.karvon.domain.use_case.shops.GetCategoryProductsUseCase
import uz.khusinov.karvon.domain.use_case.shops.GetProductsUseCase
import uz.khusinov.karvon.domain.use_case.shops.ShopsUseCases
import uz.khusinov.karvon.domain.use_case.basket.BasketUseCases
import uz.khusinov.karvon.domain.use_case.basket.GetDeliveryPriceUseCase
import uz.khusinov.karvon.domain.use_case.products.DeleteProductByShopUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCase(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            loginUseCase = LoginUseCase(repository),
            confirmUseCase = ConfirmUseCase(repository),
            logoutUseCase = LogoutUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideOrdersUseCase(repository: OrdersRepository): OrdersUseCases {
        return OrdersUseCases(
            getOrdersUseCase = GetOrdersUseCase(repository),
            getOrdersHistoryUseCase = GetOrdersHistoryUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideShopsUseCase(repository: ShopsRepository): ShopsUseCases {
        return ShopsUseCases(
            getProductsUseCase = GetProductsUseCase(repository),
            getCategoryProductsUseCase = GetCategoryProductsUseCase(repository)
        )
    }


    @Provides
    @Singleton
    fun provideProductsUseCase(repository: ProductsRepository): ProductsUseCases {
        return ProductsUseCases(
            getProductsUseCase = uz.khusinov.karvon.domain.use_case.products.GetProductsUseCase(
                repository
            ),
            insertProductUseCase = InsertProductUseCase(repository),
            removeProductUseCase = RemoveProductUseCase(repository),
            updateProductUseCase = UpdateProductUseCase(repository),
            deleteAllProductUseCase = DeleteAllProductUseCase(repository),
            deleteProductByShopUseCase = DeleteProductByShopUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCategorysUseCase(repository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(
            getCategoryUseCase = GetCategoryUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideHomeUseCase(repository: HomeRepository): HomeUseCases {
        return HomeUseCases(
            getAdsUseCase = GetAdsUseCase(repository),
            getMostSellProductsUseCase = GetMostSellProductsUseCase(repository),
            getTopProductsUseCase = GetTopProductsUseCase(repository),
            searchProductsUseCase = SearchProductsUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideProfileRepository(repository: ProfileRepository): ProfileUseCases {
        return ProfileUseCases(
            getMeUseCase = GetMeUseCase(repository),
            getCardListUseCase = uz.khusinov.karvon.domain.use_case.profile.GetCardListUseCase(repository),
            addCardUseCase = uz.khusinov.karvon.domain.use_case.profile.AddCardUseCase(repository),
            verifyCardUseCase = uz.khusinov.karvon.domain.use_case.profile.VerifyCardUseCase(repository),
            removeCardUseCase = uz.khusinov.karvon.domain.use_case.profile.RemoveCardUseCase(repository),
            setMainCardUseCase = uz.khusinov.karvon.domain.use_case.profile.SetMainCardUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideBasketUseCase(repository: BasketRepository): BasketUseCases {
        return BasketUseCases(
            basketUseCase = BasketUseCase(repository),
            addressUseCase = AddressUseCase(repository),
            getDeliveryPriceUseCase = GetDeliveryPriceUseCase(repository)
        )
    }
}