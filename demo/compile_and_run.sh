#!/bin/bash

# Compile and run Gebeta Game without Maven
# Make sure JavaFX SDK is downloaded and set JAVAFX_PATH

echo "Compiling Gebeta Game..."

# Set your JavaFX path here (adjust as needed)
# Download JavaFX from: https://openjfx.io/
JAVAFX_PATH="${JAVAFX_PATH:-/path/to/javafx-sdk-17/lib}"

if [ ! -d "$JAVAFX_PATH" ]; then
    echo "Error: JavaFX SDK not found at $JAVAFX_PATH"
    echo "Please download JavaFX SDK from https://openjfx.io/"
    echo "Then set JAVAFX_PATH environment variable or edit this script"
    exit 1
fi

# Compile
javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml *.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running game..."
    java --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml GebetaGameGUI
else
    echo "Compilation failed!"
    exit 1
fi

