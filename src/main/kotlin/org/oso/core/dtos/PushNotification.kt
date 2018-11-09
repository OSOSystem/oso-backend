package org.oso.core.dtos

data class PushNotification(val to: String, val data: String, val title: String, val body: String)