#!/bin/sh

{
./javagen < source 2> parse_doc
echo "}"
} | ( cat QueryParser.pre - ) > QueryParser.java
