
# Minecraft-Mod-Manager
A Java application to work with loading, unloading, and downloading mods into the `mods` folder in the default `.minecraft` folder

# Installing
- Ensure Java 17+ is installed
- Run the latest release

# How it works
This application essentially just moves the mods between two folders to load and unload them.
- The 'Load' button opens a file chooser to load mods
- The 'Unload' button moves the selected file in the list to the `unloaded_mods` folder
- The 'Download Mods From File' downloads mods from a `.txt` file of your choosing
- Settings button is unfinished as of now

# Mods.txt file formatting
```
modID_modFileID
```
for example:
```
328085_4762215 # Create
```
As seen above, `#` indicate comments, meaning you can put the name of the mod next to the ID's

# Notes
- Only works on Windows, hasn't been tested for MacOS or Linux
- This really wasn't made for people outside of a specified group, but if you want to try using it go ahead