package com.proyek.infrastructures.order.order.network

import com.proyek.infrastructures.order.order.`interface`.IBuyer
import com.proyek.infrastructures.order.order.`interface`.ICourier
import com.proyek.infrastructures.order.order.`interface`.IOrderable
import com.proyek.infrastructures.order.order.network.body.*
import retrofit2.Call
import retrofit2.http.*

interface OrderApiServices {
    @FormUrlEncoded
    @POST("order/create")
    fun createOrder(@Field("id") id: String, @Field("taker") taker: ICourier, @Field("buyer") buyer: IBuyer, @Field("orders") orders: List<IOrderable>, @Field("orderPrice") orderPrice: Int, @Field("feePrice") feePrice: Int, @Field("deliverDestination") deliverDestination: String): Call<OrderResponse>

    @POST("order/create/get_customer/titip_belanja")
    fun createTitipBelanjaOrder(@Body TitipBelanjaOrder: TitipBelanjaBody) : Call<OrderResponse>

    @POST("order/create/get_customer/lapor_kerusakan")
    fun createLaporKerusakanOrder(@Body LaporanKerusakanOrder: LaporanKerusakanBody) : Call<OrderResponse>

    @POST("order/create/get_customer/psb")
    fun createPSBOrder(@Body PSBOrder: PSBBody) : Call<OrderResponse>

    @POST("order/create/get_customer/ganti_paket")
    fun createGantiPaketOrder(@Body GantiPaketOrder: GantiPaketBody): Call<OrderResponse>

    @POST("order/create/get_customer/sopp")
    fun createSOPPOrder(@Body SOPP: SOPPBody) : Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/create/get_customer/titip_paket")
    fun createTitipPaketOrder(@Field("senderName") senderName: String, @Field("senderPhoneNumber") senderPhoneNumber: String, @Field("senderAddress") senderAddress: String,
                              @Field("receiverName") receiverName: String, @Field("receiverPhoneNumber") receiverPhoneNumber: String, @Field("receiverAddress") receiverAddress: String,
                              @Field("packageName") packageName: String, @Field("packageDescription") packetDescription: String, @Field("serviceId") serviceId: String, @Field("agentId") agentId: String,
                            @Field("customerName") customerName: String, @Field("customerAddress") customerAddress: String, @Field("customerPhoneNumber") customerPhoneNumber: String, @Field("customerCluster") customerCluster: String) : Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/create/get_customer/layanan_bebas")
    fun createLayananBebasOrder(
        @Field("name") name: String,
        @Field("description") descripton: String,
        @Field("serviceId") serviceId: String,
        @Field("customerName") customerName: String,
        @Field("customerAddress") customerAddress: String,
        @Field("customerPhoneNumber") customerPhoneNumber: String,
        @Field("customerCluster") customerCluster: String,
        @Field("agentId") agentId: String) : Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/create/get_customer/layanan_bebas")
    fun createLayananBebasOrder(@Field("name") name: String, @Field("description") descripton: String, @Field("serviceId") serviceId: String, @Field("customerId") customerId: String, @Field("agentId") agentId: String) : Call<OrderResponse>

    @GET("order/get/data")
    fun getOrderDetail(@Query("orderId") id: String): Call<OrderResponse>

    @GET("order/all")
    fun getOrderList(): Call<OrderResponse>

    @GET("order/get/agent_created")
    fun getAgentCreatedOrder(@Query("agentId") customerId: String, @Query("serviceType") serviceType: Int): Call<OrderResponse>

    @GET("order/get/agent_created_general")
    fun getAgentAllCreatedOrder(@Query("agentId") agentId: String) : Call<OrderResponse>

    @GET("order/get/agent_unfinished")
    fun getAgentUnfinishedOrder(@Query("agentId") customerId: String): Call<OrderResponse>

    @GET("order/get/agent_finished")
    fun getAgentFinishedOrder(@Query("agentId") customerId: String): Call<OrderResponse>

    @GET("order/get/customer_created")
    fun getCustomerCreatedOrder(@Query("customerId") customerId: String, @Query("serviceType") serviceType: String): Call<OrderResponse>

    @GET("order/get/customer_ongoing")
    fun getCustomerOngoingOrder(@Query("customerId") customerId: String, @Query("serviceType") serviceType: String): Call<OrderResponse>

    @GET("order/get/customer_unfinished")
    fun getCustomerUnfinishedOrder(@Query("customerId") customerId: String): Call<OrderResponse>

    @GET("order/get/customer_finished")
    fun getCustomerFinishedOrder(@Query("customerId") customerId: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/update")
    fun updateOrder(@Field("id") id: String, @Field("taker") taker: ICourier, @Field("buyer") buyer: IBuyer, @Field("orders") orders: List<IOrderable>, @Field("orderPrice") orderPrice: Int, @Field("feePrice") feePrice: Int,@Field("deliverDestination") deliverDestination: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/update/customer_review")
    fun updateCustomerReview(@Field("orderId") orderId: String, @Field("customerReview") customerReview: String) : Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/update/agent_note")
    fun updateAgentNote(@Field("orderId") orderId: String, @Field("agentNote") agentNote: String) : Call<OrderResponse>

    @DELETE("order/data")
    fun deleteOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/accept")
    fun acceptOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/finish_agent")
    fun agentFinishOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/finish_customer")
    fun customerFinishOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/confirm")
    fun confirmPayment(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/arrived")
    fun confirmOrderPackageArrival(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/verify")
    fun verifyOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/decline")
    fun declineOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/assign")
    fun assignOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/unassign")
    fun unassignOrder(@Field("orderId") id: String): Call<OrderResponse>

    @FormUrlEncoded
    @POST("order/finish")
    fun finishOrder(@Field("orderId") id: String): Call<OrderResponse>
}