package com.github.leandroborgesferreira.storyteller.intronotes.dynamo

import com.github.leandroborgesferreira.storyteller.intronotes.persistence.entity.StoryStepEntity
import com.github.leandroborgesferreira.storyteller.intronotes.persistence.repository.INTRO_NOTES_TABLE
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


val dynamoClient: DynamoDbClient = DynamoDbClient.builder()
    .region(Region.US_EAST_1)
    .build()

val enhancedDynamoClient: DynamoDbEnhancedClient =
    DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoClient)
        .build()

val introNotesTable: DynamoDbTable<StoryStepEntity> =
    enhancedDynamoClient.table(INTRO_NOTES_TABLE, TableSchema.fromBean(StoryStepEntity::class.java))
