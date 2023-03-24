#!/bin/bash
for anno in `cat annos.txt`; do echo "$anno :" ;  rg --count-matches -w --stats "$anno" wpi-annotations/; done