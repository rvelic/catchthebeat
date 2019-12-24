# CatchTheBeat

A variation of [grocery list](http://www.way-of-the-mind.com/memory-games.html) memory game.

## Gameplay

The game starts by generating a number of random beats. A player is given a number of percussion instruments to reproduce the sequence and adds one beat of his choice. 

Each beat must be hit in a limited timeframe set by level difficulty (easy, medium, high). The subsequent player must repeat the sequence without errors and adds another beat. If the player fails to reproduce the sequence or misses the beat in the specified timeframe he or she loses the game. The game continues until the last player fails to repeat the sequence. The score of the game is the number of beats in current sequence. The last player (winner) can beat the high score from previous games. 

The game can be played by up to 10 players, in case of single-player mode there will be second player controlled by computer who is intended to be always correct.

*The purpose of the second player is to allow some time for the first player to remember the sequence.*

## How to run (Mac OSX)
1. Make sure JDK is installed and in your PATH
2. `git clone git@github.com:rvelic/catchthebeat.git && cd ./catchthebeat`
3. `cp -R ./src/catchthebeat/sounds ../sounds`
4. `javac -classpath $(pwd)/src/ src/catchthebeat/Main.java`
5. `cd ./src && java -cp $(pwd) catchthebeat.Main`

### Credits

Developed as part of a programming excercise by Roman Velic and Michal Kabat.

CatchTheBeat is released under the [MIT License](LICENSE).