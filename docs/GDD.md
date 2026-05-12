# Game Design Document (GDD)

**Game Title:** Star Defender  
**Genre:** 2D Vertical Scrolling Space Shooter (Endless Runner)  
**Platform:** Desktop (libGDX)  
**Document Version:** 1.0  
**Date:** May 12, 2026  
**Team:** Nurdaulet, Abushakhman, Zhalgas

## 1. Game Summary
Star Defender is a fast-paced 2D vertical scrolling space shooter. The player controls a spaceship that constantly flies upward through endless space. The core gameplay loop consists of shooting enemies, dodging their attacks, and collecting power-ups. The game ends when the player's health reaches zero. The main goal is to survive as long as possible and achieve the highest score. The game is developed using libGDX for Desktop.

## 2. Controls
- **Movement:**
  - W / ↑ - Move Up
  - S / ↓ - Move Down
  - A / ← - Move Left
  - D / → - Move Right
- **Shoot:** Space (auto-fire while held)
- **Pause:** P
- **Ultimate Ability:** Q (powerful shot with cooldown)
- **Menu:** Left Mouse Click

## 3. Player
**Stats:**
- Health (HP): 100
- Speed: 300 pixels/second
- Starting weapon: Single Shot

**Abilities:**
- Collect power-ups to change weapon type
- Ultimate ability (Q) - strong attack with long cooldown
- Short invulnerability after taking damage

## 4. Enemies and Objects

**Enemies:**
- **Fast Enemy** - small, very fast, low HP (20)
- **Tank Enemy** - large, slow, high HP (80)
- **Shooter Enemy** - medium speed, shoots at player (40 HP)
- **Boss** - appears every ~400 points, very strong with multiple attack phases

**Objects:**
- **Power-ups:** Weapon Upgrade or Health Pack (+30 HP)
- Projectiles from player and enemies

## 5. Levels
The game features one endless vertically scrolling level. The background scrolls down continuously with parallax effect. Difficulty increases over time - enemies spawn more frequently and move faster. A Boss appears every 400 points. There are no separate levels — the challenge is pure survival and score chasing.

## 6. Win / Lose Conditions
- **Win Condition:** There is no traditional win. The player tries to get the highest possible score (High Score is saved).
- **Lose Condition:** Player’s HP reaches 0 → Game Over screen.
- **Game Over Screen** shows final score, high score, and buttons: **Restart** and **Main Menu**.

## 7. Art Style and Audio
- **Visual Style:** Retro / classic space shooter with bright neon colors on dark space background.
- All sprites are PNG with transparent background.
- Simple animations: explosions, player death, muzzle flash.
- Clean and nice UI / HUD (health bar, score, ultimate cooldown).
- **Audio:** Shooting sounds, explosion sounds, background music, power-up pickup sound, game over sound.

## 8. Design Patterns Used
- **State Pattern** - managing different screens (Main Menu, Playing, Pause, Game Over)
- **Strategy Pattern** - different weapon types
- **Factory + Prototype** - creating different enemies
- **Singleton** - GameManager
- **Observer** - handling game events (enemy death, power-up collected)
