#/bin/sh
file=$1
lines=$(wc -l < $file)
lines=$((0 + lines))
if [[ "${lines}" -ne 0 ]]; then
  echo "There are unexpected dependencies on:"
  cat $file
fi
exit $lines
