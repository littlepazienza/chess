#!/bin/bash

CHESS_DIR=/home/ben/chess/release
DEPLOY=deployments
DNLD=downloads

sudo rm -rf $CHESS_DIR/$DEPLOY/* $CHESS_DIR/$DNLD/*
cd $CHESS_DIR
git checkout master
git pull
npm install
electron-packager . chess --all

mv $CHESS_DIR/chess-* $CHESS_DIR/$DEPLOY
VERSION=($(ls $CHESS_DIR/$DEPLOY))

for i in "${VERSION[@]}"
do
	echo zipping $i
	zip -r $CHESS_DIR/$DNLD/$i.zip $CHESS_DIR/$DEPLOY/$i
done
