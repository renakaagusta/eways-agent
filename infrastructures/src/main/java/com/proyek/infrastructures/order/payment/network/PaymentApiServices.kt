package com.proyek.infrastructures.order.payment.network

import com.proyek.infrastructures.order.order.network.OrderResponse
import com.proyek.infrastructures.order.payment.`interface`.IMethod
import com.proyek.infrastructures.order.payment.`interface`.IPayable
import com.proyek.infrastructures.order.payment.`interface`.IReceiver
import com.proyek.infrastructures.order.payment.`interface`.ISender
import com.proyek.infrastructures.order.payment.entities.PaymentStatus
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.sql.Timestamp

interface PaymentApiServices {
    @POST("order/payment/create")
    fun createPayment(@Query("payables") payables: List<IPayable>, @Query("method") method: IMethod, @Query("total") total: Int, @Query("status") status: PaymentStatus, @Query("sender") sender: ISender, @Query("receiver") receiver: IReceiver, @Query("receiptPath") receiptPath: String, @Query("paidAt") paidAt: Timestamp, @Query("verifiedAt") verifiedAt: Timestamp): Call<OrderResponse>
//
    @GET("order/payment/data")
    fun getPaymentDetail(@Query("payables") payables: List<IPayable>, @Query("method") method: IMethod, @Query("total") total: Int, @Query("status") status: PaymentStatus, @Query("sender") sender: ISender, @Query("receiver") receiver: IReceiver, @Query("receiptPath") receiptPath: String, @Query("paidAt") paidAt: Timestamp, @Query("verifiedAt") verifiedAt: Timestamp): Call<OrderResponse>

    @GET("order/payment/all")
    fun getPaymentList(@Query("payables") payables: List<IPayable>, @Query("method") method: IMethod, @Query("total") total: Int, @Query("status") status: PaymentStatus, @Query("sender") sender: ISender, @Query("receiver") receiver: IReceiver, @Query("receiptPath") receiptPath: String, @Query("paidAt") paidAt: Timestamp, @Query("verifiedAt") verifiedAt: Timestamp): Call<OrderResponse>

    @DELETE("order/payment/data")
    fun deletePayment(@Query("payables") payables: List<IPayable>, @Query("method") method: IMethod, @Query("total") total: Int, @Query("status") status: PaymentStatus, @Query("sender") sender: ISender, @Query("receiver") receiver: IReceiver, @Query("receiptPath") receiptPath: String, @Query("paidAt") paidAt: Timestamp, @Query("verifiedAt") verifiedAt: Timestamp): Call<OrderResponse>

    @POST("order/payment/create")
    fun updatePayment(@Query("payables") payables: List<IPayable>, @Query("method") method: IMethod, @Query("total") total: Int, @Query("status") status: PaymentStatus, @Query("sender") sender: ISender, @Query("receiver") receiver: IReceiver, @Query("receiptPath") receiptPath: String, @Query("paidAt") paidAt: Timestamp, @Query("verifiedAt") verifiedAt: Timestamp): Call<OrderResponse>
}