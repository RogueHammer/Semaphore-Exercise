# Steven Mare - mrxste008
# Makefile for OS Assignment 2
# 21 May 2018

file = "sample.txt"

default: help

clean:
	rm -f *.class

compile:
	javac Simulator.java

run: clean compile
	java Simulator $(file)

help:
	more README