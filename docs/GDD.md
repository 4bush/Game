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
  - A / ← - Move Left
  - D / → - Move Right
- **Shoot:** Space
- **Pause:** ESC
- **Menu:** Left Mouse Click

## 3. Player
**Stats:**
- Lives: 3
- Speed: 300 pixels/second
- Starting weapon: Single Shot

**Abilities:**
- Short invulnerability after taking damage (TBD)

## 4. Enemies and Objects

**Enemies:**
- **Slow Enemy** - medium speed, low HP (2)
- **Fast Enemy** - small, very fast, low HP (1)
- **Tank Enemy** - large, slow, high HP (5)
- **Zigzag Enemy** - moves in a zigzag pattern
- **Boss** - appears every ~400 points, very strong with multiple attack phases

**Objects:**
- Projectiles from player

## 5. Levels
The game features one endless vertically scrolling level. The background scrolls down continuously with parallax effect. Difficulty increases over time - enemies spawn more frequently and move faster. A Boss appears every 400 points. There are no separate levels — the challenge is pure survival and score chasing.

## 6. Win / Lose Conditions
- **Win Condition:** There is no traditional win. The player tries to get the highest possible score (High Score is saved).
- **Lose Condition:** Player’s Lives reach 0 → Game Over screen.
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
