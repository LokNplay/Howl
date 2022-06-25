package com.looker.core_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedText(
	text: String,
	modifier: Modifier = Modifier,
	style: TextStyle = MaterialTheme.typography.h4,
	maxLines: Int = 1,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textColor: Color = Color.Unspecified,
	textAlign: TextAlign = TextAlign.Center,
) {
	Text(
		modifier = modifier.animateContentSize(),
		text = text,
		style = style,
		maxLines = maxLines,
		overflow = overflow,
		textAlign = textAlign,
		color = textColor,
	)
}

@Composable
fun TitleSubText(
	modifier: Modifier = Modifier,
	title: String,
	subText: String,
	titleTextStyle: TextStyle = MaterialTheme.typography.body2,
	subTextTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
	titleFontStyle: FontStyle? = null,
	subTextFontStyle: FontStyle? = null,
	itemTextAlignment: Alignment.Horizontal,
	textColor: Color = MaterialTheme.colors.onBackground,
	textAlign: TextAlign = TextAlign.Start,
	maxLines: Int = 3
) {
	Column(
		modifier = modifier.padding(horizontal = 8.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = itemTextAlignment
	) {
		Text(
			text = title,
			style = titleTextStyle,
			textAlign = textAlign,
			fontStyle = titleFontStyle,
			color = textColor,
			maxLines = maxLines,
			overflow = TextOverflow.Ellipsis
		)
		Text(
			text = subText,
			style = subTextTextStyle,
			textAlign = textAlign,
			fontStyle = subTextFontStyle,
			color = textColor.copy(0.7f),
			maxLines = maxLines,
			overflow = TextOverflow.Ellipsis
		)
	}
}