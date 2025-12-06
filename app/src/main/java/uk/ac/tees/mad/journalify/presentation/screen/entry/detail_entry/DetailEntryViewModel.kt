package uk.ac.tees.mad.journalify.presentation.screen.entry.detail_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.data.remote.CloudinaryUploader
import uk.ac.tees.mad.journalify.domain.repository.LocalJournalRepository
import uk.ac.tees.mad.journalify.domain.repository.RemoteJournalRepository
import uk.ac.tees.mad.journalify.domain.usecase.GetEntryByIdUseCase
import javax.inject.Inject

@HiltViewModel
class DetailEntryViewModel @Inject constructor(
    private val getEntry: GetEntryByIdUseCase,
    private val local: LocalJournalRepository,
    private val remote: RemoteJournalRepository,
    private val uploader: CloudinaryUploader
) : ViewModel() {

    private val _state = MutableStateFlow(DetailEntryState())
    val state: StateFlow<DetailEntryState> = _state

    fun load(id: String) {
        viewModelScope.launch {
            getEntry(id).collectLatest {
                _state.value = DetailEntryState(it)
            }
        }
    }

    fun delete(onDone: () -> Unit) {
        val e = _state.value.entry ?: return

        viewModelScope.launch {

            // remove cloud meta
            remote.delete(e.id)

            // try remove cloud asset
            e.imagePath?.let {
                try {
                    uploader.delete(e.id)
                } catch (_: Exception) {
                }
            }

            // remove local
            local.delete(e)

            onDone()
        }
    }
}