# Star Defender - Space Shooter

A dynamic 2D vertical scrolling space shooter (endless runner) built with libGDX as part of the SDP course project.

### Status
**In Progress - Day 3**

### Tech Stack
- Java
- libGDX
- Gradle

### Goal
Practice applying important design patterns in a real game project while strictly following the official **Game Dev Student Guideline** (Planning → Design → Build → Test → Ship).

---

### Design Patterns Used

- **Singleton** — `GameManager` (global state: score, high score, difficulty)
- **Factory + Prototype** — `EnemyFactory` (creates FastEnemy, TankEnemy, ShooterEnemy)
- **State Pattern** — Screen management (`MainMenuState`, `PlayingState`, `PauseState`, `GameOverState`)
- **Strategy Pattern** (planned) — Different weapon behaviors
- **Observer Pattern** (planned) — Event system

---

### Progress (Up to Day 3)

**✅ Completed:**
- Full libGDX project setup
- Player movement (WASD / Arrow keys)
- Player shooting (Space)
- GameManager (Singleton pattern)
- Enemy system with Factory + Prototype patterns
- Basic collision detection
- Complete Game Design Document (GDD)
- All 3 required diagrams:
  - Game Flow Diagram
  - Class Diagram
  - Level Sketch
- Foundation of State Pattern for screen management
- Basic Main Menu and Pause Screen implementation

**M1 – Design Done** — Successfully completed

---


Move,WASD or Arrow Keys

Shoot,Space (auto-fire)

Pause,P

Menu Navigation,Mouse Left Click

---

###Documentation

Game Design Document (GDD)

Game Flow Diagram

Class Diagram

Level Sketch


---

###Project Structure

Star-Defender/

├── core/src/           # Main game logic

├── assets/             # Sprites, sounds, fonts

├── docs/               # GDD and diagrams

├── lwjgl3/             # Desktop launcher

├── build.gradle

└── README.md

---

Team

[Zhalgas] — Project Lead / Lead Programmer

[Nurdaulet] — Programmer

[Abushakhman] — Designer / Programmer


---

### How to Run

Using IntelliJ IDEA:

Open the project in IntelliJ
Run DesktopLauncher class from the lwjgl3 module


**Recommended way:**
```bash
./gradlew desktop:run






