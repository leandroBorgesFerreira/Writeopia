---
sidebar_position: 0
---

# Basics

![Maven Central](https://img.shields.io/maven-central/v/com.github.leandroborgesferreira/writeopia-core)

Add the project in your gradle file:

```kotlin
// This will change to implementation("io.writeopia:writeopia-models:[version]") in next version
implementation("com.github.leandroborgesferreira:writeopia-models:[version]")
// This will change to implementation("io.writeopia:writeopia-core:[version]") in next version
implementation("com.github.leandroborgesferreira:writeopia-core:[version]")
```

Check releases for [latest](https://github.com/leandroBorgesFerreira/writeopia/releases) version.

## Configuration

Before displaying the content on the screen. It is necessary to configure `WriteopiaManager` which controls the state of the content, the `Drawer`s which are the classes responsible for each component of the edit and `WriteopiaEditor` which is the Composable responsible for drawing the whole editor on the screen.

## WriteopiaManager

The class `WriteopiaManager` accepts many parameters, which will be covered in a different section, but you can simply call the constructor to have the default behavior:

```kotlin
@Composable
fun WriteopiaSample() {
  WriteopiaManager()
}
```

## Drawers

Each part of the text edition is drawn by the `WriteopiaDrawer`. This interface has the logic to draw one type of information from the text. There are many drawers already implemented and available in the `DefaultDrawers` factory. Provide the `WriteopiaManager` for the default behavior:

```kotlin
DefaultDrawers.create(
  manager = noteEditorViewModel.WriteopiaManager 
)
```

## Display content

The `Composable` responsible for drawing the text editor is `WriteopiaEditor`. It needs at least a map with Drawers, the`WriteopiaManager` and a `DrawState` to draw.

```kotlin
@Composable
fun WriteopiaSample() {
  val drawState = DrawState(
    stories = mapOf(
      0 to DrawStory(
        StoryStep(type = "message", text = "Some text"),
        isSelected = false
      ),
    )
  )
  
  WriteopiaEditor(
    modifier = Modifier.fillMaxWidth().weight(1F),
    storyState = drawState,
    drawers = DefaultDrawers.create(
      manager = WriteopiaManager()
    )
  )
}
```

The above code should display a simple message on the screen and you should be able to interact with the text editor.

![sample](../../static/img/basics_sample.png)
