#!/bin/bash

RED='\033[0;31m'
BLUE='\033[1;34m'
NC='\033[0m' # No Color
if [[ $(command -v tput) && "$(tput colors)" -ge 8 ]]; then
  COLOR_CAPABILITY=true;
fi

# Log at info level (blue)
function logi {
  if [[ $COLOR_CAPABILITY ]]; then
    printf "${BLUE}${*}${NC}\n"
  else
    echo $*
  fi
}
