#!/bin/sh
dir=$(dirname "$1")
base=$(basename "$1" .pre)
base=$(basename "$base" .bp)

function expand_include
{
	for i in $(grep "^#include" "$1/$2" | sed -e 's/#include//')
	do
		expand_include "$1"/$(dirname "$i") $(basename "$i")
	done
	echo "$1/$2"
}

shift
{
cat $(expand_include "$dir" "$base.bp") $* | javagen 2> "$dir/$base".out
echo "}"
} | ( cat "$dir/$base".pre - ) > "$dir/$base".java
