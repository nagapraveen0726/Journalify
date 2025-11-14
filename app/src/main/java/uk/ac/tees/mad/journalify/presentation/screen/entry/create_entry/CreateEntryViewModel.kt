package uk.ac.tees.mad.journalify.presentation.screen.entry.create_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.domain.usecase.CreateEntryUseCase
import javax.inject.Inject

@HiltViewModel
class CreateEntryViewModel @Inject constructor(
    private val createEntry: CreateEntryUseCase
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

            createEntry(
                title = s.title,
                content = s.content,
                imagePath = s.imagePath
            )

            _uiState.value = s.copy(loading = false)
            onSuccess()
        }
    }
}
