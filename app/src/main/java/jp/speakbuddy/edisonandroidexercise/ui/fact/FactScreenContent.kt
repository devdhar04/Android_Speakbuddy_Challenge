package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState

@Composable
fun FactScreenContent(
    uiState: FactScreenState,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            when (uiState) {
                is FactScreenState.Success -> {
                    Text(text = uiState.fact)
                    if (uiState.showMultipleCats) {
                        Text(text = stringResource(R.string.multiple_cats), style = MaterialTheme.typography.titleLarge, color = Color.Red)
                    }
                    if (uiState.showFactLength) {
                        Text(text = stringResource(R.string.fact_length, uiState.factLength), style = MaterialTheme.typography.bodySmall)
                    }
                    uiState.imageUrl?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = stringResource(R.string.cat_image),
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }

                is FactScreenState.Error -> Text(text = stringResource(R.string.error, uiState.errorMessage))
                is FactScreenState.Loading -> CircularProgressIndicator()
            }
        }

        Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.update_fact))
        }
    }
}