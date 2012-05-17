#!/bin/bash
clear
echo "======================================="
echo "= Installing build dependecies DGC 4. ="
echo "======================================="
echo " "
echo "Please enter your root password to continue:"
sudo port selfupdate
sudo port install nodejs npm phantomjs
sudo port uninstall phantomjs
sudo cp phantomjs /usr/local/bin
sudo npm install -g jake --root ~/.node_modules
cd ~
mkdir .node_modules
sudo npm install wrench glob minimatch lodash jshint less uglify-js ncss phantom --root ~/.node_modules
echo "======================================="
echo "=       Installation completed!       ="
echo "======================================="
