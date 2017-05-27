package org.vince.kotlindemo.customers

import java.math.BigDecimal

typealias TurnoverByCountry = Map<String, BigDecimal>

fun String.parseCurrency(): Double = this.substring(1).replace(',', '.').toDouble()
