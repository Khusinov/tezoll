package uz.khusinov.karvon.data.remote

import FullAddress
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.khusinov.karvon.domain.model.AdsResponse
import uz.khusinov.karvon.domain.model.ConfirmRequest
import uz.khusinov.karvon.domain.model.ConfirmResponse
import uz.khusinov.karvon.domain.model.CreateOrderRequest
import uz.khusinov.karvon.domain.model.CreateOrderResponse
import uz.khusinov.karvon.domain.model.Data
import uz.khusinov.karvon.domain.model.DeliveryPrice
import uz.khusinov.karvon.domain.model.GetDeliveryPriceRequest
import uz.khusinov.karvon.domain.model.LoginRequest
import uz.khusinov.karvon.domain.model.Order
import uz.khusinov.karvon.domain.model.OrderHistory
import uz.khusinov.karvon.domain.model.Refresh
import uz.khusinov.karvon.domain.model.User
import uz.khusinov.karvon.domain.model.base.BasePagination
import uz.khusinov.karvon.domain.model.base.BaseResponseList
import uz.khusinov.karvon.domain.model.base.BaseResponseObject
import uz.khusinov.karvon.domain.model.card.AddCardRequest
import uz.khusinov.karvon.domain.model.card.AddCardResponse
import uz.khusinov.karvon.domain.model.card.CardResponse
import uz.khusinov.karvon.domain.model.card.CardVerify
import uz.khusinov.karvon.domain.model.card.VerifyCardResponse
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.domain.model.shop.Categorys
import uz.khusinov.karvon.domain.model.shop.Product
import uz.khusinov.karvon.domain.model.shop.Shops


interface ApiService {

    @POST("auth/send-sms/")
    suspend fun login(@Body phone: LoginRequest): BaseResponseObject<Unit>

    @POST("auth/logout/")
    suspend fun logout(@Body refresh: Refresh): BaseResponseObject<Unit>

    @POST("auth/verify-sms/")
    suspend fun confirm(
        @Body confirmRequest: ConfirmRequest
    ): BaseResponseObject<ConfirmResponse>


    @GET("shop/")
    suspend fun getShops(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20,
    ): BaseResponseObject<Shops>

    @GET("product/")
    suspend fun getShopsProducts(@Query("shop") shopId: Int): BaseResponseObject<BasePagination<Product>>

    @GET("product/")
    suspend fun searchProducts(@Query("search") query: String): BaseResponseObject<Data<Product>>

    @GET("shop")
    suspend fun getOrders(): BaseResponseList<Order>

    @GET("category")
    suspend fun getCategories(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): BaseResponseObject<Categorys>

    @GET("category/{id}")
    suspend fun getCategory(
        @Path("id") id: Int
    ): BaseResponseObject<CategoryRespons>

    @GET("ads")
    suspend fun getAds(): BaseResponseObject<AdsResponse>

    @POST("calc-delivery-price/")
    suspend fun getDeliveryPrice(@Body getDeliveryPriceRequest: GetDeliveryPriceRequest): BaseResponseObject<DeliveryPrice>

    @GET("product/?new_product=True")
    suspend fun getNewProducts(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): BaseResponseObject<BasePagination<Product>>

    @GET("product/?top_product=True")
    suspend fun getTopProducts(): BaseResponseObject<Data<Product>>

    @GET("product/?most_sell=True")
    suspend fun getMostSoldProducts(): BaseResponseObject<Data<Product>>

    @GET("auth/user")
    suspend fun getMe(): BaseResponseObject<User>

    @GET("orders")
    suspend fun getOrdersHistory(): BaseResponseObject<Data<OrderHistory>>

    @GET("product/")
    suspend fun getCategoryProducts(@Query("category") categoryId: Int): BaseResponseObject<BasePagination<Product>>

    @GET("http://194.87.215.154:8081/reverse.php?format=jsonv2")
    suspend fun getAddressFromLocation(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): FullAddress

    @POST("orders/")
    suspend fun createOrder(
        @Body createOrderRequest: CreateOrderRequest
    ): BaseResponseObject<Unit>

    @GET("card/")
    suspend fun getCards(): BaseResponseList<CardResponse>

    @POST("card/add/")
    suspend fun addCard(
        @Body addCardRequest: AddCardRequest
    ): BaseResponseObject<AddCardResponse>

    @POST("card/{id}/remove/")
    suspend fun deleteCard(@Path("id") id: String): BaseResponseObject<String>

    @POST("card/{id}/set-main/")
    suspend fun setMainCard(@Path("id") id: String): BaseResponseObject<String>

    @POST("card/verify/")
    suspend fun confirmCard(@Body confirm: CardVerify): BaseResponseObject<VerifyCardResponse>
}
