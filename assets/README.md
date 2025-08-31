# **Game Project README**

## **1. Project Background Overview**
-------------Game:Space Dilemma-------------

In the vast expanse of the universe, a research spaceship called "Aurora" was on a critical mission: searching for alien planets suitable for human habitation. However, as the ship traversed a mysterious nebula, it suddenly lost control and was dragged into an unknown gravitational vortex, ultimately crashing into the orbit of an uncharted planet.

When you awaken, you discover the ship's power systems are severely damaged, and all communication equipment is completely offline. To make matters worse, every level of the spaceship is infested with mysterious alien creatures. These are native entities from the nebula, drawn to the ship's energy sources. They are not only consuming the ship's resources but also launching relentless attacks against you. To survive, you must also avoid small gravitational black holes that could pull you into oblivion.

As the sole survivor aboard the ship, your mission is:

Find the manual to repair the teleportation gate and use it to return to Earth.
Fend off the alien creatures and protect the ship's critical systems.
Ultimately discover a way to escape and ensure your survival.
Good luck, survivor. The fate of the Aurora and your life lies in your hands.

## **2. Project Structure**

```
├── core/                  
│   ├── GameScreen.java    
│   ├── MenuScreen.java   
│   ├── MazeRunnerGame.java   
├   ├── HUD.java  
├   ├── GameObject.java           
├── assets/                # Resources (images, sounds, maps,etc.)
├── desktop/               # Desktop project (Windows/macOS launcher)
├── README.md              # This file
```

## **3. Class Function**

The main class function of the game is as follows:

- `GameScreen` extends `Screen` and handles game logic and rendering.
- `MenuScreen` manages the main menu logic(including screens when you pause,lose,or win the game).
- `MazeRunnerGame`controls screen transitions,loads animation and initialize game resources.
- `HUD`creates the arrow which leads to the exit of the maze

## **4. How to Run**
Both macOS and Windows use Desktoplauncher

## **5. Game Controls**

| Key             | Action                  |
|-----------------|-------------------------|
| `A`/`D`/`W`/`S` | Move left/right/up/down |
| `J`             | Attack                  |
| `F`             | Celebrate               |
| `H`             | Stop celebrating        |
| `I`             | Toggle help             |
| `P`             | Pause the game          |
| `↑` /`↓`        | Zoom in/out             |
| `ENTER`         | Enter the game          |
| `ESC`           | Back to the main Menu   |
| `SHIFT`         | Accelerate              |

## **6. Obstacles**

- **Enemies--------HEALTH-5**
- **Fire--------HEALTH-10**
- **Attractive Black hole--------HEALTH-2**


## **7.Available Items**

- **Coin--------Relate to your final score**
- **Acceleration Box--------Acceleration**
- **Heart--------HEALTH+50**


## **8.Game Rules**
- **In total, there are 5 levels,and you will get a tutorial in level1**
- **You have 100 HEALTH at the beginning of the game**
- **STAY ALIVE before you get out,pay attention to those potential dangers**
- **If you are stuck in the black hole, press shift to accelerate your speed in order to get rid of it**
- **Don't forget to press 'J' to kill those enemies**
- **You have to collect a key before getting out**
- **Follow the arrow to find the exit**
- **1 coin equals to 1 score in the end**

## **9. Game Features**
- **THE THIRD OBSTACLES----Black hole**
- **You will get a hint in terminal(where the enemies exist) when you enter the game**
- **Press F to celebrate your success**
- **Through pressing 'J', you are able to kill the enemies**
- **A tutorial is given at the beginning of Level1**
- **Fantastic Sound effect**

## **10. UML Class Diagram**

![URL.png](http://www.plantuml.com/plantuml/png/VLHDSzem4BtpAzISI1CoqsicamcXf32XXI7Cj3agycAhs997MgUX7_-zMiaW8sWlrkezyVPvzSoTHMwmgShUEooW0bQNNAfUJvJSMlQ9Lp1_-Gu2sQyUOuDrgJcojrXkgPGAMG6yn28IjWR8tFOY1_pMFxzc7YR04j0AdRSddfCJ3cZrToUUI2-U-hDpE2guuGB1U8CN-ss_O-EIHOVdyQTZ1TKsl1VnkAs1wXUjI-2AIViHk7y5Vt1d-PArpgX8Mf24DsmWQmHCVCU-HxoQVu5Mdy2cpZZ2Gbk9KgksGmObyexDAK8LN5ArSYsEh0fTbY0m39nEdJ1xy6Cl3wgf8amT9kFXSkNMqNpwnIqFq-NOBO_ZP_SSZaRZsN0vN4tdnDtFXgF7oNms9gKObYTX05Im-xcnKh0AhEKvFEb6POJwWd54BZO9k85jWgovHJ09M3G64oW3ZaM2lKg1sgIAxQXIIU17cXMenbVnNLwSvzmGvlU7QNc8NXSbtqgL7_AJrlPfTg4Dyd8eKBw2RyX_mh9H2WoDbUMyyl3SOA5pm-j2Yf7336V2BqJEfCKR7zPR1cwHOFSmtVCja-xfiVZPY5qLSgioC0T1Z3drzukkEE950M937KPkkjAwBDkG-hhauGB6wpK83A15NTyVJhBVoVkaoK9-NAyScAMdJtz5MswC_k7BQ2p81kzTfMI6GRGs4k6UeoYOZRMdDb8nwnwqwOGvtvV-OYWMWQZs9VLtnTvBhbUQ3iOTwOMTcrtcPpAq5j1s3KVjT0B_oyGAth0nq4bdYqHN2zvO28QkcLUDt5Uf3XYoq_dVl_az6CH_sIF6n-q8fImubJZ3mU0skOwKZH8f4vidHEmTGTywmIXIANJsuUhg_HcZY_WN5xxndlknHuy_lLulkH4w5hLEiV66KpOrTSodngBMgV56DhLr1ofhgl8l)
