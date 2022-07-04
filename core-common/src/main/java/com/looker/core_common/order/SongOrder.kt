package com.looker.core_common.order

import com.looker.core_common.OrderType

sealed class SongOrder(val order: OrderType) {
	class Title(order: OrderType) : SongOrder(order)
	class Duration(order: OrderType) : SongOrder(order)

	fun copy(order: OrderType): SongOrder = when (this) {
		is Title -> Title(order)
		is Duration -> Duration(order)
	}
}