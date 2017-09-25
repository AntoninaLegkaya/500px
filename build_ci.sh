#!/usr/bin/env bash

##############################################################################
##
##  Jenkins script for UN*X
##
##############################################################################

git submodule update --init --recursive

chmod +x ./gradlew
typeset err_code
./gradlew check
err_code=$?

exit $err_code