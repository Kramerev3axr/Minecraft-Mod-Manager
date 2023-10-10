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
.jar file name from link to curseforge mod
link to file download
```
for example:
```
BiomesOPlenty-1.19.2-17.1.2.544.jar
https://www.curseforge.com/minecraft/mc-mods/biomes-o-plenty/files/4473556
create-1.19.2-0.5.1.e.jar
https://www.curseforge.com/minecraft/mc-mods/create/files/4762215
```

# Notes
- Only works on Windows, hasn't been tested for MacOS or Linux
- This really wasn't made for people outside of a specified group, but if you want to try using it go ahead