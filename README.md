# Chess

## A Java-based Chess Game

This is a project I started with my son.
Recently he started learning chess and he is also interested in programming.
So I combined these two things and started this project.
As he is progressing in learning the rules and tactics we will enhance this project.

For me it is a project where I can test some techniques and technologies.

## Techniques and Technologies

- Maven
- Java 9
- Java FX
- JUnit 5
- Regular Expressions
- ANTLR 4

## Modules

### Game Module

This module contains the core of the game.
It provides handling of boards and pieces as well as rules for setup and moving.
It also includes implementations of the **Forsyth-Edwards Notation (FEN)** for game setup and storing as well as for the **Standard Algebraic Notation (SAN)** for moves.

### FX Module

For user interaction a graphical user interface is required.
This module separates the user interfaces from the rest and is based on Java FX.

### PGN Module

The **Portable Game Notation (PGN)** is a text-based database format storing chess games.
This module provides access to the database files.