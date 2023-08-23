package com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class DocumentEntity(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("id")
    var id: String? = null,
    @get:DynamoDbAttribute("title")
    val title: String? = "",
    @get:DynamoDbAttribute("content")
    val content: List<StoryStepEntity>? = emptyList(),
    @get:DynamoDbAttribute("createdAt")
    val createdAt: Long? = null,
    @get:DynamoDbAttribute("lastUpdatedAt")
    val lastUpdatedAt: Long? = null,
)
