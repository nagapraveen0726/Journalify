package uk.ac.tees.mad.journalify.presentation.screen.entry.create_entry

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.domain.usecase.CreateEntryUseCase
import uk.ac.tees.mad.journalify.domain.usecase.UploadImageUseCase
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateEntryViewModel @Inject constructor(
    private val createEntry: CreateEntryUseCase,
    private val uploadImage: UploadImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEntryUiState())
    val uiState: StateFlow<CreateEntryUiState> = _uiState

    fun updateTitle(v: String) {
        _uiState.value = _uiState.value.copy(title = v)
    }

    fun updateContent(v: String) {
        _uiState.value = _uiState.value.copy(content = v)
    }

    fun updateImage(path: String?) {
        _uiState.value = _uiState.value.copy(imagePath = path)
    }

    fun save(onSuccess: () -> Unit) {
        val s = _uiState.value
        if (s.title.isBlank() && s.content.isBlank()) return

        viewModelScope.launch {
            _uiState.value = s.copy(loading = true)

            try {
                // Upload image first if present
                val cloudinaryUrl = s.imagePath?.let { localPath ->
                    Log.d("CreateEntryVM", "Uploading image from: $localPath")
                    val entryId = UUID.randomUUID().toString()
                    val url = uploadImage(entryId, localPath)
                    Log.d("CreateEntryVM", "✅ Image uploaded to: $url")
                    url
                }

                Log.d("CreateEntryVM", "Creating entry with imageUrl: $cloudinaryUrl")

                createEntry(
                    title = s.title,
                    content = s.content,
                    imageUrl = cloudinaryUrl  // Changed from imagePath to imageUrl
                )

                _uiState.value = s.copy(loading = false)
                onSuccess()

            } catch (e: Exception) {
                Log.e("CreateEntryVM", "Failed to save entry", e)
                _uiState.value = s.copy(loading = false)
            }
        }
    }
}