package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.speakbuddy.edisonandroidexercise.BuildConfig
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

        AsyncImage(
            model = BuildConfig.CAT_URL,
            contentDescription = "Cat Image",
            modifier = Modifier.size(300.dp)
        )


        when {
            catFactResult.isSuccess -> {
                val factResponse = catFactResult.getOrNull()
                if (factResponse != null) {
                    if (factResponse.fact.contains("cats", ignoreCase = true)) {
                        Text(text = "Multiple cats!", style = MaterialTheme.typography.bodyMedium, color = Color.Red) // Prominent display
                    }
                    if (factResponse.length > 100) {
                        Text(text = "Fact Length: ${factResponse.length}", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(
                        text = catFactResult.getOrNull()?.fact ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else{
                    Text(
                        text =  "No fact Found !",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
            catFactResult.isFailure -> {
                Text(
                    text = "Error: ${catFactResult.exceptionOrNull()?.message ?: "Unknown error"}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

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
