# JavaFXImpostorGame
controls: wasd, t, y, arrow keys 

This program demonstrates my understanding of OOP in java and basic knowledge on JavaFX.  
The program is a game where you control the main character, a blue circle. The blue circle
is capable of walking to rooms directly north, south, east, and west of him and can only travel
to one room at a time. Additionally, the blue circle is equipped with an infinite amount of small
white circles (light orbs) that he can fire into any room directly north, east, south, or west of 
him. His objective is to hit a 'fake shape' (the ellipse) hiding among the other shapes in the grid. 
Firing light orbs at the real shape characters will not harm them or hinder the game.
Random rooms are occupied by random objects. The rooms with other circles and rectangles in them cannot 
be entered. However, a warning will display when one of those shapes is nearby. Other random rooms may
contain traps symbolized by arcs. If the blue circle walks into one of those rooms he will die and
the game will end abruptly. No warning messages will be displayed when a trap is near. One room in the 
grid will contain an ellipse. A special message will display whenever you are near that room. If you 
hit the ellipse with a white circle you reveal him and win the game and the GUI will close. The walking 
controls are the UP, DOWN, LEFT, and RIGHT arrow keys. The firing keys are W for up, A for left, S for 
down, and D for right. The debugging mode can be accessed by pressing the T key which will reveal the 
contents of every room, and it can be turned off by pressing the Y key which will immerse everything 
in darkness once again.
