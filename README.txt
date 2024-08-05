# Arena.java

**Author Name**: Qabas Imbewa  
**File Name**: Arena.java  
**Description**: Pokemon text-based game!

## Overview

Arena.java is a text-based Pokemon battle game where players can choose their Pokemon team and battle against random enemy Pokemon. The game continues until either the player's team or the enemy team is completely defeated.

## How to Play

1. **Load Pokemon**: The game loads the Pokemon from a file named `pokemon.txt`.
2. **Choose Players**: The player selects their team of Pokemon.
3. **Battle**: The player battles against randomly selected enemy Pokemon.
4. **Win or Lose**: The game ends when either the player's team or the enemy team is completely defeated.

## Game Mechanics

- **Loading Pokemon**: Pokemon are loaded from a file where each line contains details about a Pokemon.
- **Choosing Players**: The player selects a team of up to 4 Pokemon.
- **Battle Rounds**: Each battle consists of multiple rounds where players and enemies take turns to attack.
- **Healing**: At the end of each battle, the player's remaining Pokemon heal some HP.
- **Victory or Defeat**: The game announces whether the player has won or lost after all battles are concluded.

## Code Details

### Main Class: Arena

- **Variables**:
  - `ArrayList<Pokemon> pokemons`: List of all available Pokemon.
  - `ArrayList<Pokemon> goodGuys`: List of player's chosen Pokemon.
  - `int USER, ENEMY`: Constants representing the user and enemy turns.

- **Methods**:
  - `main(String[] args)`: Entry point of the program.
  - `load()`: Loads Pokemon from `pokemon.txt`.
  - `choosePlayers()`: Allows the player to select their team.
  - `displayTeam()`: Displays the player's selected team.
  - `battle()`: Conducts a battle between the player's team and a random enemy.
  - `heal(Pokemon enemy)`: Heals the player's Pokemon and the enemy.
  - `userTurn(Pokemon firstPlayer, Pokemon enemy)`: Handles the user's turn.
  - `AITurn(Pokemon enemy, Pokemon firstPlayer)`: Handles the AI's turn.
  - `canRetreat()`: Checks if the player can retreat.
  - `getRandomEnemy()`: Selects a random enemy Pokemon.
  - `pickFirstPlayer()`: Allows the player to pick the starting Pokemon.
  - `retreat(Pokemon currentPlayer)`: Handles the retreat action.
  - `healHP(ArrayList<Pokemon> goodGuys)`: Heals HP of the player's Pokemon after a battle.

### Class: Pokemon

- **Variables**:
  - `String name, type, resistance, weakness`: Attributes of the Pokemon.
  - `int hp, energy, numAttacks`: Health, energy, and number of attacks.
  - `boolean stunned, disabled`: Status effects.
  - `ArrayList<Attack> attacks`: List of attacks available to the Pokemon.

- **Methods**:
  - `Pokemon(String[] pok)`: Constructor to create a Pokemon object.
  - `getName()`: Returns the Pokemon's name.
  - `isStunned()`: Checks if the Pokemon is stunned.
  - `unstun()`: Unstuns the Pokemon.
  - `isAwake()`: Checks if the Pokemon is awake (HP > 0).
  - `resetEnergy()`: Resets the Pokemon's energy.
  - `attack(Pokemon enemy, int attackType)`: Performs an attack on the enemy.
  - `wildStorm(Pokemon enemy, Attack attack, int num)`: Handles the "wild storm" special attack.
  - `pickAction(boolean attack, boolean canRetreat)`: Prompts the user to pick an action.
  - `canAfford()`: Checks if the Pokemon can afford any attacks.
  - `pickRandomAttack()`: Selects a random attack.
  - `pickAttack()`: Prompts the user to pick an attack.
  - `heal()`: Heals the Pokemon's energy.
  - `healHP()`: Heals the Pokemon's HP.
  - `getStats()`: Returns the Pokemon's stats.
  - `toString()`: Returns a string representation of the Pokemon.

### Class: Attack

- **Variables**:
  - `String attackName, special`: Name and special effect of the attack.
  - `int cost, damage`: Cost and damage of the attack.

- **Methods**:
  - `Attack(String attackName, int cost, int damage, String special)`: Constructor to create an Attack object.
  - `hasSpecial()`: Checks if the attack has a special effect.
  - `toString()`: Returns a string representation of the attack.

## How to Run

1. Ensure you have `pokemon.txt` in the same directory as `Arena.java`.
2. Compile the Java file: `javac Arena.java`.
3. Run the program: `java Arena`.

## `pokemon.txt` Format

This file format should contain the number of Pokemon, followed by each Pokemon's details, including attacks and special abilities.
