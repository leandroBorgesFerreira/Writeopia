package com.github.leandroborgesferreira.storyteller.intronotes

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.github.leandroborgesferreira.storyteller.intronotes.write.writeIntroNotes
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WriteIntroNotes : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent,
        context: Context
    ): APIGatewayProxyResponseEvent =
        writeIntroNotes(
            input.body,
            loggerFn = { logger.debug(it) },
            loggerErrorFn = { logger.error(it) })
}
