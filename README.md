# Makula Android

The Android version of the Makula app.

Use a markdown editor to read / edit this readme file, e.g. [https://macdown.uranusjr.com](https://macdown.uranusjr.com).

Last changed: 2018/09/13

## Project

Developed with: Android Studio 3.2.1 and Kotlin

Min API level: 21 (Android 5 Lollipop)

The main reason for the min API level is the required support for the text-to-speech feature which is only available beginning with this level.

### Structure

The project uses the MVVM pattern. Code is separated by their responsibility into the following folders:

- **Base**: Contains super classes to derive from.
- **Constants**: Global project constants.
- **Extensions**: General class extensions.
- **Global**: Some classes used globally in the project.
- **Scenes**: The app's views where each activity and its companion classes are further put into their own subfolders.
- **Types**: Some global accessible types.
- **Utils**: Utility classes.
- **Views**: Global views not belonging to a specific scene.
- **Worker**: General logic separated from the scene.

### Flavor

The project uses the `devProduction` flavor during development for filling the database with test data.

The `finalProduction` flavor is used to release the app where test methods and dummy data is not included.

### Version

The file `version.properties` contains the app's build number and version number. This numbers are used in code and the app.

- **VERSION_MAJOR**: The major version number, e.g. the `1` in `1.2.3 (99)`. Is not automatically increased and should generally not be changed (without a very good reason).
- **VERSION_MINOR**: The minor version number, e.g. the `2` in `1.2.3 (99)`. Is not automatically increased and should be set to `0` for a major version change. Increase it by one manually when adding new features to the app.
- **VERSION_PATCH**: The patch version number, e.g. the `3` in `1.2.3 (99)`. Is not automatically increased and should be set to `0` for a major or minor version change. Increase it by one manually for a bugfix version.
- **VERSION_BUILD**: The build number, e.g. the `99` in `1.2.3 (99)`. Is automatically incremented with each project build and should not be altered manually. Each released version needs to have a version higher than a previous release.
