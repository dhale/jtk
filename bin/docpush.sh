#!/bin/bash

####################################################################
# A script to push the javadocs to github. You must have permission
# to push changes to the jtk repository. If you have not set up some
# SSH authentication, then pushing the javadocs to github will prompt
# you to enter your github username and password. 
# 
# The recommended option is to use SSH over the HTTPS port:
#   https://help.github.com/articles/using-ssh-over-the-https-port
# Another option is to generate an SSH key and add it to github:
#   https://help.github.com/articles/generating-ssh-keys
#
# NOTE: This script assumes git is on your PATH.
###################################################################

# Get the current branch so we can switch back to it at the end
CURRENT_BRANCH=`git branch --no-color | awk '/*/ {print $2}'`
echo "Currently in branch $CURRENT_BRANCH"

# Move the docs to tmp. If we merge or force the checkout
# then we will lose the local changes
mv build/doc/api /tmp

# Try to switch to the gh-pages branch
git checkout gh-pages || exit $?

# Delete what we currently have and get the latest docs
rm -rf javadoc && mv /tmp/api javadoc

# Add, commit, and push to github
git add javadoc || exit $?
git commit javadoc -m "Generated javadocs" || exit $?
git push origin || exit $?

# give the push a chance to report back
sleep 5

# Switch back to the branch we were originally in
git checkout $CURRENT_BRANCH || \
  echo "Failed to switch back to branch $CURRENT_BRANCH"
