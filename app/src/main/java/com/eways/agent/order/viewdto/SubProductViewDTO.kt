package com.eways.agent.order.viewdto

data class SubProductViewDTO (
    val subproductImage : String?,
    val subproductName :String,
    val subproductDescription : String,
    val subproductPrice: Int
){
    var subproductAmount: Int = 0
}