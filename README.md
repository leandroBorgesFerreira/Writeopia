
<p align="center" width="100%">
  <a href="https://github.com/leandroBorgesFerreira/Writeopia/actions/workflows/main-merge.yml"><img src="https://github.com/leandroBorgesFerreira/Writeopia/actions/workflows/main-merge.yml/badge.svg" alt="Main merge"></a>
  <a href="https://search.maven.org/artifact/io.writeopia/writeopia-core"><img src="https://img.shields.io/maven-central/v/io.writeopia/writeopia-core" alt="Maven Central"/></a>
  <a href="https://pinterest.github.io/ktlint/" target="_blank"><img src="https://img.shields.io/badge/ktlint%20code--style-%E2%9D%A4-FF4081" alt="ktlint badge" />
  </a>
</p>

<p align="center" width="100%">
  <img 
   src="./images/icon_with_title.svg" alt="Writeopia logo"
   width="100" 
   height="87" 
  />
</p>

<p align="center">Open source documentation app for the brave and true <p/>

<p align="center" width="100%">
  <img 
   src="./images/usage_screenshot.png" alt="Writeopia logo"
  />
</p>

This is the source code for Writeopia, an text edition App and SDK. 

# Application
Writeopia is a project develop with Kotlin Multiplatform, most of the code is shared between all the apps. This is a project to save all your documents in one place providing you the freedom to choose where to store it. You can save your whole workspace with a button click and save it in git repository (like Github) or a storage system like Dropbox or Google Drive. A backend end is also understand develop so you can self host it and have full controll how you share your documentations with work teams and friends. 

## Documentation
You can check the documentation of the project in: [https://docs.writeopia.io](https://docs.writeopia.io).

## Sample 
The sample app of the project is a great way to get a simple demo. You can have an experience like this those: [https://sample.writeopia.io](https://sample.writeopia.io) 

## Development
You can check how to [work locally](https://docs.writeopia.io) <!-- Todo: Change this link!  --> if you would like to contribute with fixes, improvements or try Writeopia from source code. 

## Contributing
Writeopia is a free project, so we would love your participation. Please check our issues and suggest changes before using the issues tab so we can evaluate and architecture new solutions together. 

If you're looking for a place to get started you can look at:
- Issues with [good first issue](https://github.com/leandroBorgesFerreira/Writeopia/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22) label
- [Documentation](https://github.com/leandroBorgesFerreira/Writeopia/tree/Documentation/documentation/writeopia_docs) for the project
- Bugs and other issues listed on GitHub
- Analise the apps and looks for new bugs. 

## Commands
Some useful commands for this project:

To start the desktop app:

```
./gradlew application:desktopApp:run
```

To start the web app:

```
./gradlew jsBrowserRun
```

To start the android app use Android Studio.

# SDK 
The SDK allows you to create a text editor an use it instead of a simple `TextField`. You can use this library to create a better story editing experience within apps, such as social media (posts), travel logs, blog apps (like Medium), and chats (like Slack). By using this library, you can provide a great experience for editing text with media.


