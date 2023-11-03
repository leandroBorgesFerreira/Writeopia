package io.writeopia.common.uitests

object CommonTests {

    fun testAddTitleAndContent(documentEditRobot: DocumentEditRobot) {
        val title = "Some Title"
        val text = "Some text"

        documentEditRobot.run {
            writeTitle(title)
            checkWithTitle(title)
            addLine()
            writeText(text, 2)
            checkWithText(text)
        }
    }
}