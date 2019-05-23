# Meeting May the 23th
Present:
- Steven
- Jelmer
- Thijs
- Bram
- Robert (late)

# Abilities
## Player
Will be saved under the player class.
- Melee attack
- Ranged attack
- (Healing)

## Enemies
- Melee attack -> slime can jump
- Ranged attack -> skeleton can shoot arrow

## Boss
(Not a priority at the moment.)

# UI
- Heart with number next to it
- Joystick
- Topleft menu button
- Three ability buttons on the bottomleft

# Lobby 
- Choose the ability

# Game engine
- Pre-condition: party with abilities
- Move (takes an angle)
- Update level every tick
	- Call AI for enemies
	- Do submitted action for players
	- Send update data to clients

# Enemies
- AI