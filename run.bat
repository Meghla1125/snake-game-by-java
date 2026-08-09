@echo off
javac SnakeGame.java
if errorlevel 1 pause && exit /b 1
java SnakeGame
