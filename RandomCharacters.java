package cs2012final;

/*Class that populates the main pane in the Main class with rectangles, circles, an ellipse,
 * and arcs in random spaces on the grid
 * */

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomCharacters {
	protected static final int TILE_SIZE = 50;

	private final int numRows;
	private final int numColumns;
	private final Pane pane;

	public RandomCharacters(int numRows, int numColumns, Pane pane) {
		this.numRows = numRows;
		this.numColumns = numColumns;
		this.pane = pane;
	}

	public Set<Integer> populateGrid(Circle mainCharacter) {
		Random random = new Random();
		
		//using a hash because it doesn't allow duplicate values & order of our locations doesn't matter
		Set<Integer> generatedLocations = new HashSet<>();

		int mainCharacterRow = (int) (mainCharacter.getCenterY() / TILE_SIZE);
		int mainCharacterColumn = (int) (mainCharacter.getCenterX() / TILE_SIZE);
		int mainCharacterLocationHash = mainCharacterRow * numColumns + mainCharacterColumn;
		generatedLocations.add(mainCharacterLocationHash);

		// Generate 3 random cells for any combination of 3 rectangles or circles
		while (generatedLocations.size() < 4) {
			int randomRow = random.nextInt(numRows);
			int randomColumn = random.nextInt(numColumns);
			int locationHash = randomRow * numColumns + randomColumn;

			if (generatedLocations.contains(locationHash)) {
				// Skips if the locationHash is already generated so characters aren't placed on the same cell
				continue; 
			}

			generatedLocations.add(locationHash);

			// Create a rectangle or circle
			boolean isRectangle = random.nextBoolean();
			if (isRectangle) {
				Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE, Color.BLACK);
				rectangle.setFill(Color.BLACK);
				rectangle.setStroke(Color.BLACK);
				rectangle.setTranslateX(randomColumn * TILE_SIZE);
				rectangle.setTranslateY(randomRow * TILE_SIZE);
				pane.getChildren().add(rectangle);
			} else {
				Circle circle = new Circle(TILE_SIZE / 2, Color.BLACK);
				circle.setFill(Color.BLACK);
				circle.setStroke(Color.BLACK);
				circle.setTranslateX(randomColumn * TILE_SIZE + TILE_SIZE / 2);
				circle.setTranslateY(randomRow * TILE_SIZE + TILE_SIZE / 2);
				pane.getChildren().add(circle);
			}
		}

		// Create 2 arcs in random location(s)
		for (int i = 0; i < 2; i++) {
			int arcRow;
			int arcColumn;
			do {
				arcRow = random.nextInt(numRows);
				arcColumn = random.nextInt(numColumns);
			} while ((arcRow == mainCharacterRow && arcColumn == mainCharacterColumn)
					|| generatedLocations.contains(arcRow * numColumns + arcColumn));

			double arcRadiusX = TILE_SIZE / 4;
			double arcRadiusY = TILE_SIZE / 4;
			double arcStartAngle = 10;
			double arcLength = 180;
			Arc arc = new Arc(arcColumn * TILE_SIZE + TILE_SIZE / 2, arcRow * TILE_SIZE + TILE_SIZE / 2,
					arcRadiusX, arcRadiusY, arcStartAngle, arcLength);
			arc.setType(ArcType.CHORD);
			arc.setFill(Color.BLACK);
			arc.setStroke(Color.BLACK);
			pane.getChildren().add(arc);
		}




		//create ONLY ONE ellipse in a random location (this is our boss)
		int ellipseRow;
		int ellipseColumn;
		do {
			ellipseRow = random.nextInt(numRows);
			ellipseColumn = random.nextInt(numColumns);
			int ellipseLocationHash = ellipseRow * numColumns + ellipseColumn;
			generatedLocations.add(ellipseLocationHash);
		} while (ellipseRow == mainCharacterRow && ellipseColumn == mainCharacterColumn);

		double ellipseRadiusX = TILE_SIZE / 2;
		double ellipseRadiusY = TILE_SIZE / 2;
		Ellipse ellipse = new Ellipse(ellipseRadiusX, ellipseRadiusY);
		ellipse.setFill(Color.BLACK);
		ellipse.setStroke(Color.BLACK);
		ellipse.setTranslateX(ellipseColumn * TILE_SIZE + TILE_SIZE / 2);
		ellipse.setTranslateY(ellipseRow * TILE_SIZE + TILE_SIZE / 2);
		pane.getChildren().add(ellipse);

		return generatedLocations;
	}


}