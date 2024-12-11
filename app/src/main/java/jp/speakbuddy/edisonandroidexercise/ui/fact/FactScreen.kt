package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme

@Composable
fun FactScreen(
    viewModel: FactViewModel
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
        val catFactResult by viewModel.catFactResult.collectAsState()

        Text(
            text = "Fact",
            style = MaterialTheme.typography.titleLarge
        )

        when {
            catFactResult.isSuccess -> {
                Text(
                    text = catFactResult.getOrNull() ?: "", // Handle null case
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            catFactResult.isFailure -> {
                Text(
                    text = "Error: ${catFactResult.exceptionOrNull()?.message ?: "Unknown error"}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
            // You can add a loading state here if needed
        }

        Button(onClick = { viewModel.fetchCatFact() }) {
            Text(text = "Update fact")
        }
    }
}

@Preview
@Composable
private fun FactScreenPreview() {
    EdisonAndroidExerciseTheme {
       // FactScreen(viewModel = FactViewModel())
    }
}
