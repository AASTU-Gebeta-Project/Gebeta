#!/bin/bash

# Run Gebeta Game with JavaFX
# Make sure JavaFX is in your classpath or use Maven

echo "Running Gebeta Game..."

# Option 1: Using Maven (recommended)
if command -v mvn &> /dev/null; then
    echo "Using Maven to run..."
    mvn clean javafx:run
else
    echo "Maven not found. Please install Maven or run manually with JavaFX modules."
    echo ""
    echo "To run manually, use:"
    echo "java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml GebetaGameGUI"
fi

