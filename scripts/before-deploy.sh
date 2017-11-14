#!/usr/bin/bash
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "unit" ]]; then
  logi "Build Maven site"
  "$MVN" site
  "$MVN" site:stage
fi
