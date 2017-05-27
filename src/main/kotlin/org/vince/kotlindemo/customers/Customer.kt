package org.vince.kotlindemo.customers

data class Customer(
        var id: String,
        var firstname: String,
        var lastname: String,
        var email: String,
        var turnover: String?,
        var country: String
)