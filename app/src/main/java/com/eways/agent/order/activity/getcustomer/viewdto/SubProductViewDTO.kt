package com.eways.agent.order.activity.getcustomer.viewdto

data class SubProductViewDTO (
    val id: Int?,
    val itemId: String?,
    val subproductImage : String?,
    val subproductName :String,
    val subproductDescription : String,
    val subproductPrice: Int
){
    var subproductAmount: Int = 0
}