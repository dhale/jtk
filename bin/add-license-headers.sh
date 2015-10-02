#!/bin/sh

# add-license-headers.sh - Adds missing license header block to sources.

author="Colorado School of Mines and others"

year() {
	date="$(git log --format=format:%cd --date=short --reverse -- "$1" | head -1)"
	echo "${date:0:4}"
}

# Process Java sources
for f in $(grep -L Copyright $(git ls-files | grep '\.java$'))
do
	year="$(year "$f")"
	echo "$f: Adding license header"
	perl -0777 -i -pe "s/^/\/****************************************************************************\nCopyright $year, $author.\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\n    http:\/\/www.apache.org\/licenses\/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.\n****************************************************************************\/\n/igs" "$f"
done

# Process Python sources
for f in $(grep -L Copyright $(git ls-files | grep '\.py$'))
do
	year="$(year "$f")"
	echo "$f: Adding license header"
	perl -0777 -i -pe "s/^/#\/****************************************************************************\n# Copyright $year, $author.\n# Licensed under the Apache License, Version 2.0 (the \"License\");\n# you may not use this file except in compliance with the License.\n# You may obtain a copy of the License at\n#\n#     http:\/\/www.apache.org\/licenses\/LICENSE-2.0\n#\n# Unless required by applicable law or agreed to in writing, software\n# distributed under the License is distributed on an \"AS IS\" BASIS,\n# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n# See the License for the specific language governing permissions and\n# limitations under the License.\n#****************************************************************************\/\n/igs" "$f"
done
