package uz.khusinov.karvon.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.khusinov.karvon.data.local.ProductsDao
import uz.khusinov.karvon.data.remote.ApiService
import uz.khusinov.karvon.data.repository.AuthRepositoryImpl
import uz.khusinov.karvon.data.repository.BasketRepositoryImpl
import uz.khusinov.karvon.data.repository.CategoryRepositoryImpl
import uz.khusinov.karvon.data.repository.HomeRepositoryImpl
import uz.khusinov.karvon.data.repository.OrdersRepositoryImpl
import uz.khusinov.karvon.data.repository.ProductsRepositoryImpl
import uz.khusinov.karvon.data.repository.ProfileRepositoryImpl
import uz.khusinov.karvon.data.repository.ShopsRepositoryImpl
import uz.khusinov.karvon.domain.repository.AuthRepository
import uz.khusinov.karvon.domain.repository.BasketRepository
import uz.khusinov.karvon.domain.repository.CategoryRepository
import uz.khusinov.karvon.domain.repository.HomeRepository
import uz.khusinov.karvon.domain.repository.OrdersRepository
import uz.khusinov.karvon.domain.repository.ProductsRepository
import uz.khusinov.karvon.domain.repository.ProfileRepository
import uz.khusinov.karvon.domain.repository.ShopsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLoginRepository(apiService: ApiService): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideOrdersRepository(apiService: ApiService): OrdersRepository {
        return OrdersRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideShopsRepository(apiService: ApiService): ShopsRepository {
        return ShopsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(productsDao: ProductsDao): ProductsRepository {
        return ProductsRepositoryImpl(productsDao)
    }

    @Provides
    @Singleton
    fun provideCategorysRepository(apiService: ApiService): CategoryRepository {
        return CategoryRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(apiService: ApiService): HomeRepository {
        return HomeRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(apiService: ApiService): ProfileRepository {
        return ProfileRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideBasketRepository(apiService: ApiService): BasketRepository {
        return BasketRepositoryImpl(apiService)
    }
}