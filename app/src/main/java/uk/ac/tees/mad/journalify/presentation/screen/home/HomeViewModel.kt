package uk.ac.tees.mad.journalify.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.domain.usecase.GetEntriesUseCase
import uk.ac.tees.mad.journalify.domain.usecase.SearchEntriesUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEntries: GetEntriesUseCase,
    private val searchEntries: SearchEntriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        observeAll()
    }

    private fun observeAll() {
        viewModelScope.launch {
            getEntries().collect { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    entries = list
                )
            }
        }
    }

    fun search(value: String) {
        _uiState.value = _uiState.value.copy(query = value)

        viewModelScope.launch {
            if (value.isBlank()) {
                observeAll()
                return@launch
            }

            searchEntries(value).collect { list ->
                _uiState.value = _uiState.value.copy(entries = list)
            }
        }
    }
}
