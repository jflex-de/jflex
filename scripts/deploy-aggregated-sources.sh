echo "Push to https://github.com/jflex-de/jflex/tree/aggregated-java-sources"
cd repo
# SECURITY NOTICE: Be sure to send stdout & stderr to /dev/null so that the the ${GITHUB_TOKEN} is$
git remote set-url --push origin "https://${GITHUB_TOKEN}@github.com/jflex-de/jflex.git" > /dev/null
git push
