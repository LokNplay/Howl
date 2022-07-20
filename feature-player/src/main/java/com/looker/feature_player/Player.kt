package com.looker.feature_player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core_common.states.SheetsState
import com.looker.core_service.extensions.toSong
import com.looker.core_ui.components.*
import com.looker.core_ui.ext.backgroundGradient
import com.looker.feature_player.components.*
import com.looker.feature_player.queue.PlayerQueue

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel(),
	onSheetStateChange: () -> SheetsState = { SheetsState.HIDDEN }
) {
	val dominantColorState = rememberDominantColorState()
	val isPlaying by viewModel.isPlaying.collectAsState()
	val currentSong by viewModel.nowPlaying.collectAsState()
	val toggleButtonState by viewModel.toggleStream.collectAsState()
	Column(
		modifier = modifier
			.fillMaxWidth()
			.backgroundGradient { dominantColorState.color.overBackground() }
			.statusBarsPadding(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		val animateSize by animateDpAsState(targetValue = if (onSheetStateChange() == SheetsState.HIDDEN) 250.dp else 350.dp)
		AlbumArt(
			modifier = Modifier
				.width(450.dp)
				.height(animateSize),
			button = {
				LaunchedEffect(onSheetStateChange()) {
					viewModel.setBackdrop(onSheetStateChange())
				}
				val toggleColor by animateColorAsState(
					targetValue =
					if (toggleButtonState.enabled) MaterialTheme.colors.secondaryVariant
						.overBackground(0.9f)
					else MaterialTheme.colors.background
				)
				Button(
					modifier = Modifier
						.clip(MaterialTheme.shapes.medium)
						.align(Alignment.BottomEnd)
						.drawBehind { drawRect(toggleColor) },
					colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
					elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
					onClick = { viewModel.onToggleClick(toggleButtonState.toggleState) },
				) {
					val toggleIcon by remember(toggleButtonState) { mutableStateOf(toggleButtonState.icon) }
					PlayPauseIcon(
						tint = {
							if (toggleButtonState.enabled) MaterialTheme.colors.onSecondary
							else MaterialTheme.colors.onBackground
						},
						icon = { toggleIcon }
					)
				}
			},
		) {
			LaunchedEffect(currentSong.toSong.albumArt) {
				dominantColorState.updateColorsFromImageUrl(currentSong.toSong.albumArt)
			}
			val transition = updateTransition(
				targetState = isPlaying,
				label = "Album Art Transition"
			)
			val imageCorner by transition.animateInt(label = "Corner Size") {
				if (it) 50 else 15
			}
			val scale by transition.animateFloat(label = "Scale") {
				if (it) 1f else 0.95f
			}
			HowlImage(
				modifier = Modifier
					.matchParentSize()
					.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(imageCorner)
						scaleX = scale
						scaleY = scale
					},
				data = { currentSong.toSong.albumArt }
			)
		}
		SongText {
			AnimatedText(
				text = currentSong.toSong.name,
				style = MaterialTheme.typography.h2,
				maxLines = 2
			)
			AnimatedText(
				text = currentSong.toSong.artist,
				style = MaterialTheme.typography.h4,
				textColor = LocalContentColor.current.copy(0.8f)
			)
		}
		PlayerQueue()
	}
}

@Composable
fun Controls(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel()
) {
	val isPlaying by viewModel.isPlaying.collectAsState()
	val playIcon by viewModel.playIcon.collectAsState()
	val progress by viewModel.progress.collectAsState()
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		PlayAndSkipButton(skipNextClick = viewModel::playNext) {
			val buttonShape by animateIntAsState(targetValue = if (isPlaying) 50 else 15)
			OpaqueIconButton(
				modifier = Modifier
					.height(60.dp)
					.weight(3f)
					.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(buttonShape)
					},
				onClick = viewModel::playMedia,
				backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(0.9f),
				contentColor = MaterialTheme.colors.onPrimary,
				shape = RectangleShape
			) {
				PlayPauseIcon { playIcon }
			}
		}
		PreviousAndSeekBar(skipPrevClick = viewModel::playPrevious) {
			SeekBar(
				modifier = Modifier.height(60.dp),
				progress = { progress },
				onValueChange = viewModel::onSeek,
				onValueChanged = viewModel::onSeeked
			)
		}
	}
}