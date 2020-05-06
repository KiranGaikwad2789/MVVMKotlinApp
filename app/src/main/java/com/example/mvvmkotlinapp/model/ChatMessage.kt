package com.example.mvvmkotlinapp.model

data class ChatMessage(var chatId: String? =null,
    var messageId: String? =null,
    var senderId: String? =null,
    var receiverId: String? =null,
    var message: String? =null,
    var timeStamp: String? =null,
    var messageType: String? =null) {


}
