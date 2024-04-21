package io.writeopia.sdk.import_document.markdown

public class MarkdownToDocument(
    private val charactersReverseIndexParser: CharactersReverseIndexParser
) {

//    fun parse(inputReader: InputStream) {
//        inputReader.reader().forEachLine { line ->
//            charactersReverseIndexParser.
//        }
//    }

//    private fun typeOfLine(
//        line: String,
//        default: StoryType = StoryTypes.MESSAGE.type,
//        allowedCommands: Set<Command> = CommandFactory.defaultCommands()
//    ): StoryType {
//        val reversedIndex = charactersReverseIndexParser.reversedIndex
//        val commandIdsPerPosition = mutableListOf<List<Int>>()
//
//        val commands = allowedCommands.toMutableSet()
//
//        // Stop when a space is reached. The command should keep looking until a space is should. Otherwise
//        // it should keep looking/ For example ###### is not a command, but it would match it not
//        // looking until a space is found.
//
//        line.forEachIndexed { index, char ->
//            val indexes = reversedIndex[char]
//
//            if (indexes == null) {
//                return default
//            } else {
//                commandIdsPerPosition.add(indexes.map { it.commandId })
//            }
//        }
//    }
}