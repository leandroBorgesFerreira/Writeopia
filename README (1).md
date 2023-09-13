# README

![Java CI with Gradle](https://github.com/leandroBorgesFerreira/StoryTeller/workflows/Complete%20build/badge.svg) ![Maven Central](https://img.shields.io/maven-central/v/com.github.leandroborgesferreira/storyteller)

## ![logo](https://github.com/leandroBorgesFerreira/StoryTeller/assets/10619102/bd7172de-51f0-40a2-ba29-dea50c13e7b7)

\


Embeded text editor for apps.

## Intro

This SDK allows you to create a text editor an use it instead of a simple `TextField`. You can use this library to create a better story editing experience within apps, such as social media (posts), travel logs, blog apps (like Medium), and chats (like Slack). By using this library, you can provide a great experience for editing text with media.

Rather than having to post all media separately and add an `TextField` feature, the library enables media and text to be combined in a story format. This allows for a more engaging and interactive experience for users.

### Examples of usage

#### Todo app:

https://github.com/leandroBorgesFerreira/StoryTeller/assets/10619102/cd68d513-77e2-4a32-88e5-ac38999d32a1

#### Medium-Style blog text editor:

\[To do]

#### Posts editor (Social media style complex posts)

\[To do]

#### Team chat complex messages editor (Slack/Discord/etc...)

\[To do]

### Quick start

Add the project in your gradle file:

```
implementation("com.github.leandroborgesferreira:storyteller-models:[version]")
implementation("com.github.leandroborgesferreira:storyteller:[version]")
implementation("com.github.leandroborgesferreira:storyteller-persistence:[version]")
```

#### Create the library

Add `StoryTellerEditor` to your app:

```
StoryTellerEditor(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F),
        storyState = storyState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp),
        editable = editable,
        listState = listState,
        drawers = DefaultDrawers.create(
            editable,
            noteDetailsViewModel.storyTellerManager,
            groupsBackgroundColor = if (isSystemInDarkTheme()) BACKGROUND_VARIATION_DARK else BACKGROUND_VARIATION
        )
    )
```

Use `StoryTellerManager` to controll the state of the editor. It is recommended to add it to your ViewModel. You can use it to create new documents, load documents from the database, save the current state, etc.

You can check an example of created view model [in view model of the sample app](app\_sample/src/main/java/br/com/leandroferreira/app\_sample/screens/note/NoteDetailsViewModel.kt)

#### Persistence

You can use `DocumentRepository` to persist the documents you create with ease. This the [in view model of the sample app](app\_sample/src/main/java/br/com/leandroferreira/app\_sample/screens/note/NoteDetailsViewModel.kt) for examples.

### Features

This SDK allows users to edit text by changing the position of the text and media, and provides ways to interact with the list (although drag and drop functionality is still to be implemented).

By providing `Drawers`, it is possible to control and customize the way information units are displayed on the screen. Using the `StepsNormalizers` and listening to user click events, it is possible to control how the `StoryTellerEditor` behaves. The inputs can be changed to support many different use cases to suit any kind of app that needs an enhanced tool for creating user-generated content.

The following sections provide a detailed explanation of how each part of the SDK works and how to use it.

#### Drawers

Those classes explain to `StoryTellerTimeline` how each type of information should be draw in the screen. Each drawer needs to be associated with its type, to get a plug and play experience you can use `DefaultDrawers` provided by the library.

Currently the library supports by default:

* AddButtonDrawer
* ImageGroupDrawer
* ImageStepDrawer
* MessageStepDrawer
* VideoStepDrawer
* SpaceDrawer
* CheckItemDrawer

Each Drawer can be substituted to change the default behaviour of the library to suit your intended behaviour.

#### Normalization

Custom normalizers allow users to merge, change the position of, and add new `StoryUnits`. These classes are designed to modify the story data in response to user input, backend input, or app logic.

Currently the library supports:

* Merge items of the same type toghether. When a list of `StorySteps` of type "image" has the same position, they are merged into "group\_image". Instead of using `ImageStepDrawer` the new `StoryUnit` now is drawn with `ImageGroupDrawer` and a image gallery is presented to the user. Example:

![Screenshot 2023-04-17 at 23 10 16](https://user-images.githubusercontent.com/10619102/232611555-32ed2125-2622-4f69-8fde-e140751dbb2c.png)

**Please note that normalizers do not have a commutative property, so it is important to always place the PositionNormalization last. Additionally, changing the order of normalizers may affect the end result.**
