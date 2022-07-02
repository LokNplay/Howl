package com.looker.music_data

import android.content.Context
import android.provider.MediaStore
import com.looker.core_model.Song
import com.looker.music_data.utils.MusicCursor
import com.looker.music_data.utils.MusicCursor.externalUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongsData(private val context: Context) {

	companion object {
		val songsProjections = arrayOf(
			MediaStore.Audio.AudioColumns._ID,
			MediaStore.Audio.AudioColumns.ALBUM_ID,
			MediaStore.Audio.AudioColumns.TITLE,
			MediaStore.Audio.AudioColumns.ARTIST,
			MediaStore.Audio.AudioColumns.ALBUM,
			MediaStore.Audio.AudioColumns.DURATION
		)
		const val sortOrderSong = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"
	}

	suspend fun createSongsList(): List<Song> {
		val song = mutableListOf<Song>()
		getSongFlow().collect { song.add(it) }
		return song
	}

	private fun getSongFlow(): Flow<Song> = flow {

		val songCursor = MusicCursor.generateCursor(context, songsProjections, sortOrderSong)

		songCursor?.let {
			if (it.moveToFirst()) {
				do {
					val songId = it.getLong(0)
					val albumId = it.getLong(1)
					val songName = it.getString(2) ?: ""
					val artistName = it.getString(3) ?: ""
					val albumName = it.getString(4) ?: ""
					val songDurationMillis = it.getLong(5)
					val songUri = "$externalUri/$songId"
					val albumArt = "content://media/external/audio/albumart/$albumId"
					emit(
						Song(
							mediaId = songId.toString(),
							albumId = albumId,
							pathUri = songUri,
							name = songName,
							artist = artistName,
							album = albumName,
							duration = songDurationMillis,
							albumArt = albumArt
						)
					)
				} while (it.moveToNext())
			}
			it.close()
		}
	}
}