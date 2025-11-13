# Text-Based Adventure Game (Java)

**Simple text-based adventure** built as a Java console project. Demonstrates OOP concepts:
- Classes and objects (Player, Room, Item, Enemy)
- Inheritance (`Item` -> `Weapon`, `Potion`, `Key`)
- Encapsulation and modular design
- Simple combat, health, keys/locked doors, and multiple endings

## Structure
Files:
- `Game.java` (main class)
- `Player.java`
- `Room.java`
- `Enemy.java`
- `Item.java`
- `Weapon.java`
- `Potion.java`
- `Key.java`
- `README.md`
- `.gitignore`

## How to compile & run
From the project directory:
```bash
javac *.java
java Game
```

## Commands
- `go [direction]` (e.g., `go north`)
- `look`
- `take [item]` (or `take` to pick the first item)
- `inventory`
- `attack`
- `use [item]` (e.g., `use Small Potion`)
- `unlock [keyName]` (e.g., `unlock GoldenKey`)
- `help`
- `quit`


## Git workflow suggestion
1. Create repo on GitHub and clone locally.
2. Create feature branches (e.g., `feature-combat`, `feature-items`).
3. Commit frequently, open Pull Requests, review and merge.
4. Tag final release: `git tag v1.0` and `git push --tags`.

## Notes / Extensions
- You can split or expand rooms, add more enemies, or persistent save/load.
- Consider adding unit tests or more robust parsing for commands.
