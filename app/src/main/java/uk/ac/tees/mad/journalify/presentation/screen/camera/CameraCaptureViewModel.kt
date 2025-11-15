package uk.ac.tees.mad.journalify.presentation.screen.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.journalify.data.media.ImageStorage
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraCaptureViewModel @Inject constructor(
    private val storage: ImageStorage
) : ViewModel() {

    private val _photo = MutableSharedFlow<String>()
    val photo: SharedFlow<String> = _photo

    fun save(file: File) {
        viewModelScope.launch {
            val path = storage.save(file)
            _photo.emit(path)
        }
    }
}
