# Zworkout

This is an editor of workouts usable on Zwift.

# Building and Releasing the app

## Automated build status
There is not CI quite yet :(.

The binaries of releases are also available thanks to [Jitpack](https://jitpack.io). The latest release there is 
[![](https://jitpack.io/v/pijpijpij/zworkout.svg)](https://jitpack.io/#pijpijpij/zworkout).

## Build and install the app locally?

Nothing specific, use Android Studio.

## How to release the app?

<_**TBC**_>

That means creating a release version and prepare it for the next release increment. That includes setting up its SCM.
Releases are tagged with their version number (e.g. release 5.3.2 is build from the version tagged `5.3.2` in Git).

1. Checkout the head of `master` and start a command prompt
1. Run pre-release checks. Do a full build to ensure the code is good to be released.

    `> ./gradlew build`

1. Release (assuming authentication with SSH keys is already setup). If not, Bitbucket explained it well 
[here](https://confluence.atlassian.com/x/YwV9E)):

    `> ./gradlew release`

    Make sure the last output line indicates it's been *pushed to origin*.

    To set the release number, rather than accept the usual bug-level increment, add the following property on the 
    command line:

    `-Prelease.forceVersion=k.m.n`

1. Build the release version of the app to take the new version number into account:

    `> ./gradlew build install`
    
    That is only needed if you do not want to wait for [Jitpack](https://jitpack.io/#org.github.pijpijpij/zworkout/) to finish its 
    build.


The overall command is quite simple:

    > ./gradlew build release

