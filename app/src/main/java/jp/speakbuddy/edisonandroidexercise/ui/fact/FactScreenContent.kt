package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.semantics.error
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.speakbuddy.edisonandroidexercise.BuildConfig
import jp.speakbuddy.edisonandroidexercise.ui.FactScreenState

@Composable
fun FactScreenContent(
    uiState: FactScreenState,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator() // Show loading indicator
        } else if (uiState.error != null) {
            Text(text = "Error: ${uiState.error}") // Show error message
        } else {
            Text(text = uiState.fact)
            if (uiState.showMultipleCats) {
                Text(text = "Multiple cats!", style = MaterialTheme.typography.titleLarge, color = Color.Red)
            }
            if (uiState.factLength != null && uiState.factLength > 100) {
                Text(text = "Fact Length: ${uiState.factLength}", style = MaterialTheme.typography.bodySmall)
            }
            uiState.imageUrl?.let { url ->
                AsyncImage(
                    model = BuildConfig.CAT_URL,
                    contentDescription = "Cat Image",
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        Button(onClick = onRefresh) { // Call refresh action
            Text(text = "Update fact")
        }
    }
}