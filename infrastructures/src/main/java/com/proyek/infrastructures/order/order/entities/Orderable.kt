package com.proyek.infrastructures.order.order.entities

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.inventory.internetservice.entities.InternetService
import com.proyek.infrastructures.inventory.invoice.entities.Invoice
import com.proyek.infrastructures.inventory.item.entities.Grocery
import com.proyek.infrastructures.user.customer.entities.Customer

data class Orderable(

    @SerializedName("groceries")
    val groceries: ArrayList<Grocery>,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @SerializedName("service")
    val service: Service? = null,

    @SerializedName("internet_service")
    val internetService: InternetService? = null,

    @SerializedName("invoice")
    val invoice: Invoice? = null,

    @SerializedName("package_name")
    val packetName: String?= null,

    @SerializedName("package_description")
    val packetDescription: String?= null,

    @SerializedName("sender_name")
    val senderName: String? = null,

    @SerializedName("sender_address")
    val senderAddress: String? = null,

    @SerializedName("sender_latitude")
    val senderLatitude: String? = null,

    @SerializedName("sender_longitude")
    val senderLongitude: String? = null,

    @SerializedName("sender_phone_number")
    val senderPhoneNumber: String? = null,

    @SerializedName("receiver_name")
    val receiverName: String? = null,

    @SerializedName("receiver_address")
    val receiverAddress: String? = null,

    @SerializedName("receiver_latitude")
    val receiverLatitude: String? = null,

    @SerializedName("receiver_longitude")
    val receiverLongitude: String? = null,

    @SerializedName("receiver_phone_number")
    val receiverPhoneNumber: String? = null,

    @SerializedName("agent_fee")
    val agentFee: Int? = null,

    @SerializedName("service_fee")
    val serviceFee: Int? = null,

    @SerializedName("damage_description")
    val damageDescription: String? = null,

    @SerializedName("new_internet_service")
    val newInternetService: InternetService,

    @SerializedName("old_internet_service")
    val oldInternetService: InternetService,

    @SerializedName("customer")
    val customer: Customer? = null
)