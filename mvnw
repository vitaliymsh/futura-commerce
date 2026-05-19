#!/usr/bin/env bash
# Futura Commerce Portable Maven Wrapper

set -e

# Configure Java (prefer openjdk@21 LTS for maximum compatibility)
if [ -d "/opt/homebrew/opt/openjdk@21" ]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
elif [ -d "/usr/local/opt/openjdk@21" ]; then
    export JAVA_HOME="/usr/local/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
elif [ -z "$JAVA_HOME" ]; then
    if [ -d "/opt/homebrew/opt/openjdk" ]; then
        export JAVA_HOME="/opt/homebrew/opt/openjdk/libexec/openjdk.jdk/Contents/Home"
    elif [ -d "/usr/local/opt/openjdk" ]; then
        export JAVA_HOME="/usr/local/opt/openjdk/libexec/openjdk.jdk/Contents/Home"
    fi
fi

if [ -n "$JAVA_HOME" ]; then
    export PATH="$JAVA_HOME/bin:$PATH"
fi

# Locate Maven
if command -v mvn &> /dev/null; then
    exec mvn "$@"
elif [ -x "/opt/homebrew/bin/mvn" ]; then
    exec /opt/homebrew/bin/mvn "$@"
elif [ -x "/usr/local/bin/mvn" ]; then
    exec /usr/local/bin/mvn "$@"
else
    echo "Error: Maven binary not found."
    echo "Install it via: brew install maven"
    exit 1
fi
