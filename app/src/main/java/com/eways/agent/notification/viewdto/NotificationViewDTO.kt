
package com.eways.agent.notification.viewdto

import com.eways.agent.notification.const.NotificationType
import com.eways.agent.utils.date.SLDate

data class NotificationViewDTO (
    var notificationType: NotificationType,
    var date : SLDate,
    var orderType : String,
    var price : Int?,
    val type_id: String?
)