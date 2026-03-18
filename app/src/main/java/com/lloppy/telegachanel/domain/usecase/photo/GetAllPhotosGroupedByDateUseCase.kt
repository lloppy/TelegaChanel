package com.lloppy.telegachanel.domain.usecase.photo

import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetAllPhotosGroupedByDateUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    operator fun invoke(): Flow<Map<String, List<PhotoItem>>> =
        repository.getAllPhotos().map { photos ->
            val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
            photos
                .sortedByDescending { it.takenAt }
                .groupBy { dateFormat.format(Date(it.takenAt)) }
        }
}
