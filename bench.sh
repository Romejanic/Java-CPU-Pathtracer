#!/bin/bash
for i in {1..5}; do
  ./run.sh --width=100 --height=100 --samples=1 --bounces=2 2>/dev/null | \
    grep Took | awk '{print $3}' | sed 's/ms//'
done
