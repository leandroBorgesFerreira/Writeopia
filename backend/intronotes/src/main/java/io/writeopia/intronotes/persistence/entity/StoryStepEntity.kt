package io.writeopia.intronotes.persistence.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import java.util.*

@DynamoDbBean
data class StoryStepEntity(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("id")
    var id: String? = null,
    var type: String? = null,
    var parentId: String? = null,
    var url: String? = null,
    var path: String? = null,
    var text: String? = null,
    var title: String? = null,
    var checked: Boolean? = false,
    var position: Int? = null,
)