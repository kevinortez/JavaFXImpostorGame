package cs2012final;


/* Kevin Ortez (307310554) CS 2012-01 (31315) cs2012 Final Project: JavaFX game
 * This program demonstrates my understanding of OOP in java and basic knowledge on JavaFX.  
 * The program is a game where you control the main character, a blue circle. The blue circle
 * is capable of walking to rooms directly north, south, east, and west of him and can only travel
 * to one room at a time. Additionally, the blue circle is equipped with an infinite amount of small
 * white circles (light orbs) that he can fire into any room directly north, east, south, or west of 
 * him. His objective is to hit a 'fake shape' (the ellipse) hiding among the other shapes in the grid. 
 * Firing light orbs at the real shape characters will not harm them or hinder the game.
 * Random rooms are occupied by random objects. The rooms with other circles and rectangles in them cannot 
 * be entered. However, a warning will display when one of those shapes is nearby. Other random rooms may
 * contain traps symbolized by arcs. If the blue circle walks into one of those rooms he will die and
 * the game will end abruptly. No warning messages will be displayed when a trap is near. One room in the 
 * grid will contain an ellipse. A special message will display whenever you are near that room. If you 
 * hit the ellipse with a white circle you reveal him and win the game and the GUI will close. The walking 
 * controls are the UP, DOWN, LEFT, and RIGHT arrow keys. The firing keys are W for up, A for left, S for 
 * down, and D for right. The debugging mode can be accessed by pressing the T key which will reveal the 
 * contents of every room, and it can be turned off by pressing the Y key which will immerse everything 
 * in darkness once again.
 * */


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class Main extends Application {

	private static final int TILE_SIZE = 50;
	private boolean blackTilesEnabled = true;
	private boolean whiteTilesEnabled = true;
	boolean adjacentToRandomCharacter = false;
	private List<Circle> smallCircles = new ArrayList<>();



	@Override
	public void start(Stage primaryStage) {
		int[] gridSizes = getUserInput();

		if (gridSizes == null) {
			return;
		}

		int numRows = gridSizes[0];
		int numColumns = gridSizes[1];

		Pane pane = createCustomPane(numRows, numColumns);

		Scene scene = new Scene(pane);
		primaryStage.setTitle("Custom Pane Example");
		primaryStage.setScene(scene);

		primaryStage.setWidth((numColumns + 1) * TILE_SIZE);
		primaryStage.setHeight((numRows + 1) * TILE_SIZE);

		primaryStage.show();
	}

	//set-up for the input pane
	private int[] getUserInput() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Grid Size");
		dialog.setHeaderText("Enter the grid dimensions:");
		dialog.setContentText("Format = ROWSxCOLUMNS:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String[] inputValues = result.get().split("x");
			if (inputValues.length == 2) {
				try {
					int[] gridSizes = new int[2];
					gridSizes[0] = Integer.parseInt(inputValues[0]);
					gridSizes[1] = Integer.parseInt(inputValues[1]);
					return gridSizes;
				} catch (NumberFormatException e) {
					showAlert("Invalid Input", "Please enter valid integer values for the row and column sizes.");
				}
			} else {
				showAlert("Invalid Input", "Please enter two integer values for the row and column sizes separated by an x.");
			}
		}
		return null;
	}

	//input pane alert for invalid integer values
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	//messages on the GUI indicating adjacent characters
	private void showAdjacentMessage(Pane pane) {
		Text messageText = new Text("You're near a shape!");
		messageText.setFont(Font.font(12.5));
		messageText.setFill(Color.RED);
		messageText.setTranslateX(10);
		messageText.setTranslateY(pane.getHeight());
		pane.getChildren().add(messageText);
	}

	private void removeAdjacentMessage(Pane pane) {
		pane.getChildren().removeIf(node -> node instanceof Text);
	}

	//messages on GUI indicating the Ellipse or "fake shape" is nearby
	private void showEllipseMessage(Pane pane) {
		Text messageText = new Text("FIRE!!FIRE!!");
		messageText.setFont(Font.font(12.5));
		messageText.setFill(Color.GREEN);
		messageText.setTranslateX(285);
		messageText.setTranslateY(pane.getHeight());
		pane.getChildren().add(messageText);
	}

	private Pane createCustomPane(int numRows, int numColumns) {
		Pane pane = new Pane();
		pane.setPadding(new Insets(10));


		//this 2D array is how I made a grid without using GridPane
		Rectangle[][] tiles = new Rectangle[numRows][numColumns];

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE, Color.BLACK);
				tile.setStroke(Color.BLACK);
				tile.setTranslateX(col * TILE_SIZE);
				tile.setTranslateY(row * TILE_SIZE);
				pane.getChildren().add(tile);

				tiles[row][col] = tile;
			}
		}

		//random column and row code to place the mainCharacter on a random tile -causes issue with 
		//ellipse sometimes, code works perfectly if row and column are the first index i.e. 
		//mainCharacter.setCenterX(25); mainCharacter.setCenterY(25);
		Random random = new Random();
		int randomRow = random.nextInt(numRows);
		int randomColumn = random.nextInt(numColumns);
		//the controllable character in the program is 'mainCharacter'
		Circle mainCharacter = new Circle(25, Color.BLUE);
		mainCharacter.setCenterX(randomColumn * TILE_SIZE + TILE_SIZE / 2);
		mainCharacter.setCenterY(randomRow * TILE_SIZE + TILE_SIZE / 2);
		mainCharacter.setFill(Color.BLUE);
		mainCharacter.setFocusTraversable(true);
		pane.getChildren().add(mainCharacter);

		//populate the grid with our randomCharacters from the RandomCharacters class 
		//(rectangles, circles, and arcs)
		RandomCharacters randomCharacters = new RandomCharacters(numRows, numColumns, pane);
		Set<Integer> generatedLocations = randomCharacters.populateGrid(mainCharacter);

		mainCharacter.setOnKeyPressed(e -> {
			KeyCode kc = e.getCode();

			int currentRow = (int) (mainCharacter.getCenterY() / TILE_SIZE);
			int currentCol = (int) (mainCharacter.getCenterX() / TILE_SIZE);

			if (kc.isArrowKey()) {
				switch (kc) {
				//movement keys don't allow for mainCharacter to move outside of the grid
				//based on column/row size and will not allow mainCharacter to travel onto
				//the generatedLocations associated with the ellipse, or rectangles/circles 
				//created in the RandomCharacters class.
				case DOWN:
					if (currentRow < numRows - 1 && !generatedLocations.contains((currentRow + 1) * numColumns + currentCol)) {
						mainCharacter.setCenterY(mainCharacter.getCenterY() + TILE_SIZE);
						currentRow++;
					}
					break;
				case UP:
					if (currentRow > 0 && !generatedLocations.contains((currentRow - 1) * numColumns + currentCol)) {
						mainCharacter.setCenterY(mainCharacter.getCenterY() - TILE_SIZE);
						currentRow--;
					}
					break;
				case LEFT:
					if (currentCol > 0 && !generatedLocations.contains(currentRow * numColumns + (currentCol - 1))) {
						mainCharacter.setCenterX(mainCharacter.getCenterX() - TILE_SIZE);
						currentCol--;
					}
					break;
				case RIGHT:
					if (currentCol < numColumns - 1 && !generatedLocations.contains(currentRow * numColumns + (currentCol + 1))) {
						mainCharacter.setCenterX(mainCharacter.getCenterX() + TILE_SIZE);
						currentCol++;
					}
					break;
				}
				//checks if mainCharacter steps into a room with an arc - if you do, game ends
				checkMainCharacterArcIntersection(pane, mainCharacter);

				if (blackTilesEnabled) {
					tiles[currentRow][currentCol].setFill(Color.WHITE);
				}

				boolean adjacentToEllipse = false;
				boolean adjacentToRandomCharacter = false;

				for (Integer locationHash : generatedLocations) {
					int row = locationHash / numColumns;
					int col = locationHash % numColumns;

					//Math.abs used so the row and column calculations are always positive
					if (Math.abs(currentRow - row) <= 1 && Math.abs(currentCol - col) <= 1) {
						if (pane.getChildren().stream().anyMatch(node ->
						node instanceof Ellipse &&
						Math.abs(node.getTranslateX() - (col * TILE_SIZE + TILE_SIZE / 2)) < 1e-6 &&
						Math.abs(node.getTranslateY() - (row * TILE_SIZE + TILE_SIZE / 2)) < 1e-6)) {
							adjacentToEllipse = true;
						}

						adjacentToRandomCharacter = true;
					}
				}

				if (adjacentToEllipse) {
					showEllipseMessage(pane);
				} else {
					removeAdjacentMessage(pane);
				}

				if (adjacentToRandomCharacter) {
					showAdjacentMessage(pane);
				} else {
					removeAdjacentMessage(pane);
				}

				//T is the key for the debugging mode
			} else if (kc == KeyCode.T) {
				blackTilesEnabled = !blackTilesEnabled;

				if (!blackTilesEnabled) {
					for (int row = 0; row < numRows; row++) {
						for (int col = 0; col < numColumns; col++) {
							tiles[row][col].setFill(Color.WHITE);
						}
					}
				}

				//Y is the key to turn off the debugging mode
			} else if (kc == KeyCode.Y) {
				whiteTilesEnabled = !whiteTilesEnabled;

				if (!whiteTilesEnabled) {
					for (int row = 0; row < numRows; row++) {
						for (int col = 0; col < numColumns; col++) {
							tiles[row][col].setFill(Color.BLACK);
							blackTilesEnabled=true;
						}
					}
				}

				//This is the start of the W A S D controls,
				//W fires a circle in the center of the tile above mainCharacter,
				//A fires one to the left, D to the right, and S below.
			} else if (kc == KeyCode.D) {
				if (currentCol < numColumns - 1 ) {
					int adjacentRow = currentRow;
					int adjacentCol = currentCol + 1;
					double circleCenterX = (adjacentCol + 0.5) * TILE_SIZE;
					double circleCenterY = (adjacentRow + 0.5) * TILE_SIZE;

					Circle smallCircle = new Circle(5, Color.WHITE);
					smallCircle.setCenterX(circleCenterX);
					smallCircle.setCenterY(circleCenterY);
					pane.getChildren().add(smallCircle);

					// Add the small circle to the list
					smallCircles.add(smallCircle);

					checkSmallCircleEllipseCollisions(pane);
				}
			} else if (kc == KeyCode.A) {
				if (currentCol < numColumns - 1 ) {
					int adjacentRow = currentRow;
					int adjacentCol = currentCol - 1;
					double circleCenterX = (adjacentCol + 0.5) * TILE_SIZE;
					double circleCenterY = (adjacentRow + 0.5) * TILE_SIZE;

					Circle smallCircle = new Circle(5, Color.WHITE);
					smallCircle.setCenterX(circleCenterX);
					smallCircle.setCenterY(circleCenterY);
					pane.getChildren().add(smallCircle);

					// Add the small circle to the list
					smallCircles.add(smallCircle);

					checkSmallCircleEllipseCollisions(pane);
				}
			} else if (kc == KeyCode.W) {
				if (currentCol < numColumns - 1 ) {
					int adjacentRow = currentRow - 1;
					int adjacentCol = currentCol;
					double circleCenterX = (adjacentCol + 0.5) * TILE_SIZE;
					double circleCenterY = (adjacentRow + 0.5) * TILE_SIZE;

					Circle smallCircle = new Circle(5, Color.WHITE);
					smallCircle.setCenterX(circleCenterX);
					smallCircle.setCenterY(circleCenterY);
					pane.getChildren().add(smallCircle);

					// Add the small circle to the list
					smallCircles.add(smallCircle);

					checkSmallCircleEllipseCollisions(pane);
				}
			} else if (kc == KeyCode.S) {
				if (currentCol < numColumns - 1 ) {
					int adjacentRow = currentRow + 1;
					int adjacentCol = currentCol;
					double circleCenterX = (adjacentCol + 0.5) * TILE_SIZE;
					double circleCenterY = (adjacentRow + 0.5) * TILE_SIZE;

					Circle smallCircle = new Circle(5, Color.WHITE);
					smallCircle.setCenterX(circleCenterX);
					smallCircle.setCenterY(circleCenterY);
					pane.getChildren().add(smallCircle);

					// Add the small circle to the list
					smallCircles.add(smallCircle);

					checkSmallCircleEllipseCollisions(pane);
				}
			}

		});
		mainCharacter.toFront();

		return pane;
	}

	//checks if the smallCircle (projectile) intersects with the ellipse
	private void checkSmallCircleEllipseCollisions(Pane pane) {
		for (Circle smallCircle : smallCircles) {
			for (Node node : pane.getChildren()) {
				if (node instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) node;
					// literal check for the small circle intersecting with the ellipse
					if (smallCircle.getBoundsInParent().intersects(ellipse.getBoundsInParent())) {

						// Display congratulations message
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Congratulations!");
						alert.setHeaderText(null);
						alert.setContentText("You shot the fake shape! Congratulations!");
						alert.showAndWait();

						// Closes GUI once the Alert is closed or OK button is pressed
						Platform.exit();

						return; 
					}
				}
			}
		}
	}

	//check if mainCharacter falls into a room with an arc (trap)
	private void checkMainCharacterArcIntersection(Pane pane, Circle mainCharacter) {
		for (Node node : pane.getChildren()) {
			if (node instanceof Arc) {
				Arc arc = (Arc) node;
				if (mainCharacter.getBoundsInParent().intersects(arc.getBoundsInParent())) {

					// Display alert
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Alert");
					alert.setHeaderText(null);
					alert.setContentText("You fell in a trap! GAME OVER");
					alert.showAndWait();

					// Closes GUI once the Alert is closed or OK button is pressed
					Platform.exit();

					return; 
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}