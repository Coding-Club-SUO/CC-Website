#!/bin/bash
# This script sets up the local git filter to prevent leaking secrets
git config filter.placeholder-filter.clean "sed -E 's/([^=]*(password|secret)[^=]*)=.*/\1=PLACEHOLDER/Ig'"
git config filter.placeholder-filter.smudge "cat"
echo "Git filters configured successfully!"