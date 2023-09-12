package com.storiesteller.intronotes.persistence.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class DocumentEntity(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("id")
    var id: String? = null,
    @get:DynamoDbAttribute("title")
    var title: String? = null,
    @get:DynamoDbAttribute("content")
    var content: List<StoryStepEntity>? = null,
    @get:DynamoDbAttribute("createdAt")
    var createdAt: Long? = null,
    @get:DynamoDbAttribute("lastUpdatedAt")
    var lastUpdatedAt: Long? = null,
)
