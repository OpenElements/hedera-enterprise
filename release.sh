#!/bin/bash
set -e  # Exit the script if any command fails

if [ -z "$1" ]; then
  echo "Please provide version that should be released and the next snapshot version. Example: ./release.sh 0.1.0 0.2.0-SNAPSHOT"
  exit 1
fi

if [ -z "$2" ]; then
  echo "Please provide version that should be released and the next snapshot version. Example: ./release.sh 0.1.0 0.2.0-SNAPSHOT"
  exit 1
fi

if ! [[ "$1" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
  echo "The release version must be in the format A.B.C. Example: 0.1.0"
  exit 1
fi

if ! [[ "$2" =~ ^[0-9]+\.[0-9]+\.[0-9]+-SNAPSHOT$ ]]; then
  echo "The next snapshot version must be in the format A.B.C-SNAPSHOT. Example: 0.2.0-SNAPSHOT"
  exit 1
fi

NEW_VERSION="$1"
NEXT_VERSION="$2"

export $(grep -v '^#' .env | xargs)

./mvnw clean

echo "Releasing version $NEW_VERSION"
./mvnw versions:set -DnewVersion=$NEW_VERSION
./mvnw clean verify -DskipTests
git commit -am "Version $NEW_VERSION"
git push
./mvnw -Ppublication deploy -DskipTests
./mvnw -Ppublication jreleaser:full-release

echo "Setting version to $NEXT_VERSION"
./mvnw versions:set -DnewVersion=$NEXT_VERSION
git commit -am "Version $NEXT_VERSION"
git push
