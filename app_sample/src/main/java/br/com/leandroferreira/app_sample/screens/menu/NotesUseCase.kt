package br.com.leandroferreira.app_sample.screens.menu

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import br.com.leandroferreira.app_sample.data.supermarketList
import br.com.leandroferreira.app_sample.data.travelHistory
import com.github.leandroborgesferreira.storyteller.model.document.Document
import com.github.leandroborgesferreira.storyteller.persistence.repository.DocumentRepositoryImpl
import com.github.leandroborgesferreira.storyteller.persistence.sorting.OrderBy
import com.github.leandroborgesferreira.storyteller.persistence.sorting.toEntityField
import java.util.Date
import java.util.UUID

class NotesUseCase(
    private val documentRepository: DocumentRepositoryImpl,
    private val sharedPreferences: SharedPreferences
) {

    fun saveDocumentSortingPref(orderBy: OrderBy) {
        sharedPreferences.edit()
            .run { putString(ORDER_BY_PREFERENCE, orderBy.type.toEntityField()) }
            .commit()
    }

    suspend fun loadDocuments(): List<Document> =
        sharedPreferences
            .getString(ORDER_BY_PREFERENCE, OrderBy.CREATE.type.toEntityField())
            ?.let { orderBy -> documentRepository.loadDocuments(orderBy) }!!

    suspend fun mockData(context: Context) {
        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Travel Note",
                content = travelHistory(context),
                createdAt = Date(),
                lastUpdatedAt = Date()
            )
        )

        documentRepository.saveDocument(
            Document(
                id = UUID.randomUUID().toString(),
                title = "Supermarket List",
                content = supermarketList(),
                createdAt = Date(),
                lastUpdatedAt = Date()
            )
        )
    }

    companion object {
        private const val ORDER_BY_PREFERENCE = "order_by_preference"
    }
}
