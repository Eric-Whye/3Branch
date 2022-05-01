#!/bin/bash

input="labeled tag elements.txt"
inputClean="cleanTags.temp"
output="labelCount.txt"

touch $output

cut "$input" -d " " -f 2- --output-delimiter="" > $inputClean

awk -F'[][]' '{print $2}' $inputClean | tr -s '\r\n' ',' | tr -s ',' '\n' | sort | uniq -c | sort -nr -k1 > $output

sed -i 's/\x00//g' $output

rm $inputClean
