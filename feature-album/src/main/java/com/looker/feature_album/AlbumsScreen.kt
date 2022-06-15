package com.looker.feature_album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.components.BottomSheets
import com.looker.components.HowlImage
import com.looker.components.ext.backgroundGradient
import com.looker.components.rememberDominantColorState
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.core_ui.LoadingState
import com.looker.core_ui.SongItem
import com.looker.feature_album.sheet.DetailSheetContent
import com.looker.feature_album.sheet.DetailsText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumRoute(viewModel: AlbumsViewModel = hiltViewModel()) {
	val albums by viewModel.albumsState.collectAsState()
	val songs by viewModel.songsState.collectAsState()
	val currentAlbum by viewModel.currentAlbum.collectAsState()
	val bottomSheetState =
		rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
	val scope = rememberCoroutineScope()
	val bottomSheetDominantColorState = rememberDominantColorState()
	BottomSheets(
		modifier = Modifier.fillMaxSize(),
		state = bottomSheetState,
		sheetContent = {
			DetailSheetContent(
				modifier = Modifier.backgroundGradient(bottomSheetDominantColorState.color.copy(0.4f)),
				albumText = {
					DetailsText(albumName = currentAlbum.name, artistName = currentAlbum.artist)
				},
				songsList = {
					Surface(
						modifier = Modifier.padding(16.dp),
						shape = MaterialTheme.shapes.medium,
						color = MaterialTheme.colors.background
					) {
						SongsList(songs.songsState) { viewModel.playSong(it) }
					}
				}
			) {
				LaunchedEffect(currentAlbum) {
					bottomSheetDominantColorState.updateColorsFromImageUrl(currentAlbum.albumArt)
				}
				HowlImage(
					modifier = Modifier.matchParentSize(),
					data = currentAlbum.albumArt,
					shape = MaterialTheme.shapes.large
				)
			}
		}
	) {
		LazyVerticalGrid(columns = GridCells.Fixed(2)) {
			albumsList(albums.albumsState) {
				viewModel.setCurrentAlbum(it)
				scope.launch { bottomSheetState.show() }
			}
		}
	}
}

private fun LazyGridScope.albumsList(albums: AlbumUiState, onClick: (Album) -> Unit = {}) {
	when (albums) {
		is AlbumUiState.Success -> items(items = albums.albums, key = { it.albumId }) {
			AlbumItem(album = it, cardWidth = 150.dp, onClick = { onClick(it) })
		}
		AlbumUiState.Loading -> item { LoadingState() }
		AlbumUiState.Error -> item { Text(text = "Error") }
	}
}

@Composable
private fun SongsList(songs: SongUiState, onClick: (Song) -> Unit = {}) {
	when (songs) {
		is SongUiState.Success -> {
			Column {
				songs.songs.forEach {
					SongItem(onClick = { onClick(it) }, song = it)
				}
			}
		}
		SongUiState.Loading -> LoadingState()
		SongUiState.Error -> Text("Error")
	}
}