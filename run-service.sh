#!/usr/bin/env bash
# Futura Commerce Microservice Launcher Script

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ -d "$SCRIPT_DIR/futura-commerce" ]; then
    cd "$SCRIPT_DIR/futura-commerce"
else
    cd "$SCRIPT_DIR"
fi

# Export Homebrew Java if JAVA_HOME is not set or unusable (prefer openjdk@21 LTS)
if [ -d "/opt/homebrew/opt/openjdk@21" ]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
    export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
elif [ -d "/usr/local/opt/openjdk@21" ]; then
    export JAVA_HOME="/usr/local/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
    export PATH="/usr/local/opt/openjdk@21/bin:$PATH"
elif [ -d "/opt/homebrew/opt/openjdk" ]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk/libexec/openjdk.jdk/Contents/Home"
    export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"
elif [ -d "/usr/local/opt/openjdk" ]; then
    export JAVA_HOME="/usr/local/opt/openjdk/libexec/openjdk.jdk/Contents/Home"
    export PATH="/usr/local/opt/openjdk/bin:$PATH"
fi

SERVICE_NAME="$1"

if [ -z "$SERVICE_NAME" ]; then
    echo "Usage: ./run-service.sh <module-name>"
    echo "Examples:"
    echo "  ./run-service.sh futura-gateway"
    echo "  ./run-service.sh futura-admin"
    echo "  ./run-service.sh futura-order"
    echo "  ./run-service.sh futura-product"
    echo "  ./run-service.sh futura-user"
    echo "  ./run-service.sh futura-seckill"
    echo "  ./run-service.sh futura-ai"
    exit 1
fi

echo "==> Using Java: $(which java)"
java -version

if [ -f "./mvnw" ]; then
    MVN_CMD="./mvnw"
elif command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
else
    echo "Maven is not installed. Run: brew install maven"
    exit 1
fi

echo "==> Starting microservice: $SERVICE_NAME"
$MVN_CMD spring-boot:run -pl "$SERVICE_NAME"
