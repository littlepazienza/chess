#!/bin/bash

CHESS_DIR=/home/ben/chess/release
DEPLOY=deployments
DNLD=downloads
LOFI=/var/www/lofigames.co/public_html/chess

sudo rm -rf $CHESS_DIR/$DEPLOY/*

DNLDS=($(ls $CHESS_DIR/$DNLD | grep .zip))
PREV_VERSION=($(cat $CHESS_DIR/version.txt))

mkdir $CHESS_DIR/$DNLD/old/$PREV_VERSION
rm -rf $CHESS_DIR/$DNLD/old/$PREV_VERSION/*

for i in "${DNLDS[@]}"
do
       mv $CHESS_DIR/$DNLD/$i $CHESS_DIR/$DNLD/old/$PREV_VERSION/$PREV_VERSION$i
done

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
	cp -r $CHESS_DIR/$DNLD/$i.zip $LOFI
done
