package uk.ac.tees.mad.journalify.presentation.screen.entry.edit_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.domain.usecase.GetEntryByIdUseCase
import uk.ac.tees.mad.journalify.domain.usecase.UpdateEntryUseCase
import javax.inject.Inject

@HiltViewModel
class EditEntryViewModel @Inject constructor(
    private val getEntry: GetEntryByIdUseCase,
    private val updateEntry: UpdateEntryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditEntryState())
    val state: StateFlow<EditEntryState> = _state

    fun load(id: String) {
        viewModelScope.launch {
            getEntry(id).collectLatest { e ->
                _state.value = _state.value.copy(
                    entry = e,
                    title = e?.title ?: "",
                    content = e?.content ?: "",
                    imagePath = e?.imagePath
                )
            }
        }
    }

    fun updateTitle(v: String) {
        _state.value = _state.value.copy(title = v)
    }

    fun updateContent(v: String) {
        _state.value = _state.value.copy(content = v)
    }

    fun save(onDone: () -> Unit) {
        val e = _state.value.entry ?: return

        val updated = e.copy(
            title = _state.value.title,
            content = _state.value.content,
            isSynced = false
        )

        viewModelScope.launch {
            updateEntry(updated)
            onDone()
        }
    }
}