package com.looker.core_model

data class Song(
	val mediaId: String = "",
	val name: String = "",
	val artist: String = "",
	val album: String = "",
	val albumArt: String = "",
	val pathUri: String = "",
	val duration: Long = 0L
)