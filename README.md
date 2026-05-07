# Space Shooter libGDX
A simple 2D space shooter built with libGDX as part of a design patterns course project.
## Status
In progress  day 1

## Tech Stack
Java
libGDX

## Goal
Practice applying design patterns (Singleton, Factory, Observer) in a real game project.

### Singleton — `GameManager`
Manages global game state: score, lives, game over flag.
Only one instance can exist — accessed via `GameManager.getInstance()`.
This avoids passing state manually between screens and entities.

### Factory — `EnemyFactory`
Creates different enemy types (SLOW, FAST, TANK) through a single `create(type, x, y)` method.
The caller doesn't need to know construction details — just asks for a type.

## Progress
  - [x] Project setup
 - [x] Player movement + shooting
- [x] Singleton: GameManager
- [x] Enemy base class
- [x] Factory: EnemyFactory

## How to Run
Open in IntelliJ run the lwjgl3
