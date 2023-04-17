# StoryTeller - WIP

An SDK to create a editor for stories. 

You can use this library to create a better story edition withing apps. Apps for social media (posts), travel logs, blog apps (like Medium), chats (like Slack) can use the library to provide a great experience when editing text with media. 

Instead of posting all picture, videos, audios or any other media in one position of the app and add a `EditText` this library allows to put media and text together in a story line. Example of app created with the SDK:

https://user-images.githubusercontent.com/10619102/232607035-3be9444d-e66d-4e4f-a0f6-d557b63e8fcc.mov

## Quick start
[To do]

## Features

This SDK allows users to edit the text by changing position of text and the media by providing ways to interact with the list (drag and drop is still to be implemented)

By providing Drawers to it is possible to controll and customize the way the units of the information are draw in the screen. By using the StepsNormalizers and listening to the user click events it is possible to  fully controll how the `StoryTellerTimeline` behaves. The inputs can be changed to support many different use cases to suit and kind of app that needs a enhanced tool to create content by the end user. 

The next sections explain in deep how each part of the SDK works and to use it. 


### Drawers

Those classes explain to `StoryTellerTimeline` how each type of information should be draw in the screen. Each drawer needs to be associated with its type, to get a plug and play experience you can use `DefaultDrawers` provided by the library. 

Currently the library supports by default: 

- AddButtonDrawer
- ImageGroupDrawer
- ImageStepDrawer
- MessageStepDrawer
- VideoStepDrawer

Each Drawer can be substituted to change the default behaviour of the library to suit your intended behaviour. 

### Normalization

Some pieces of information can be merged, changed in position and new story units can be added all that with custom normalizers. Those classes are meant to change the information (the story) accordingly with user inpu, backend input or an app logic. 

Currently the library supports:

- Merge items of the same time toghether. When a list of `StorySteps` of type "image" has the same position, they are merged into "group_image". Instead of using `ImageStepDrawer` the new StoryUnit now is drawn with `ImageGroupDrawer` and a image gallery is presented to the user. Example: 

![Screenshot 2023-04-17 at 23 10 16](https://user-images.githubusercontent.com/10619102/232611555-32ed2125-2622-4f69-8fde-e140751dbb2c.png)

- Normalize the position of items with `PositionNormalization`. This is needed to avoid `StoryUnits` with wrong `localPosition`.

**Please be aware that normalizers don't have comutative property, so always put the `PositionNormalization` at last and remember that changing the order of normalizers may change the end result.**

