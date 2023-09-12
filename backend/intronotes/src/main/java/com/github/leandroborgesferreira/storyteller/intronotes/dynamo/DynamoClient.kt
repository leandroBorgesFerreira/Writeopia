package com.github.leandroborgesferreira.storyteller.intronotes.dynamo

import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.DocumentEntity
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.INTRO_NOTES_TABLE
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


private fun dynamoClient(): DynamoDbClient =
    DynamoDbClient.builder().region(Region.US_EAST_1).build()

private fun enhancedDynamoClient(
    dynamoClient: DynamoDbClient = dynamoClient()
): DynamoDbEnhancedClient =
    DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoClient).build()

fun introNotesTable(
    tableName: String = INTRO_NOTES_TABLE,
    dynamoClient: DynamoDbClient = dynamoClient()
): DynamoDbTable<DocumentEntity> =
    enhancedDynamoClient(dynamoClient).table(
        tableName,
        TableSchema.fromBean(DocumentEntity::class.java)
    )
