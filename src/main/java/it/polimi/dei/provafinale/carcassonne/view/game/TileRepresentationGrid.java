package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * Class TileRepresentationGrid holds the status of the game grid as String
 * representation of tiles.
 * 
 */
public class TileRepresentationGrid {

	private static final int INITIAL_GRID_DIMENSION = 3;

	private Map<Coord, String> tiles;
	private int greatX, smallX, greatY, smallY;

	/**
	 * Constructs a new instance of this class.
	 * */
	public TileRepresentationGrid() {
		this.tiles = new HashMap<Coord, String>();
	}

	/**
	 * Executes the passed update on the grid.
	 * 
	 * @param update
	 *            - the String containing the update to execute in the form
	 *            tileRepresentation, x, y
	 * */
	public void execUpdate(String update) {
		String[] split = update.split(",");
		String tileRep = split[0];
		int x = Integer.parseInt(split[1].trim());
		int y = Integer.parseInt(split[2].trim());
		Coord c = new Coord(x, y);

		if (tiles.containsKey(c)) {
			tiles.remove(c);
		} else {
			smallX = (x < smallX ? x : smallX);
			greatX = (x > greatX ? x : greatX);
			smallY = (y < smallY ? y : smallY);
			greatY = (y > greatY ? y : greatY);
		}

		tiles.put(c, tileRep);
	}

	/**
	 * Gives the textual representation of a tiles placed at a given coord.
	 * 
	 * @param coord
	 *            - the coords of desired tile
	 * */
	public String getTileRepresentation(Coord c) {
		return tiles.get(c);
	}

	/**
	 * Checks if there is a neighbor tile for a given position.
	 * 
	 * @param coord
	 *            - the coord to check
	 * @return true if there is at least a neighbor tile for the given coord.
	 * */
	public boolean hasTileNeighbor(Coord coord) {
		for (SidePosition pos : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(pos);
			if (tiles.containsKey(coord.add(offset))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the smallest value of x among the coordinates of tiles in the
	 * grid.
	 * */
	public int smallestX() {
		return smallX - 1;
	}

	/**
	 * Returns the greates value of x among the coordinates of tiles in the
	 * grid.
	 * */
	public int greatestX() {
		return greatX + 1;
	}

	/**
	 * Returns the smallest value of y among the coordinates of tiles in the
	 * grid.
	 * */
	public int smallestY() {
		return smallY - 1;
	}

	/**
	 * Returns the greatest value of y among the coordinates of tiles in the
	 * grid.
	 * */
	public int greatestY() {
		return greatY + 1;
	}

	/**
	 * Convert a coord referred to normal system (with 0,0 in the top-left
	 * corner) to coords referred to grid system.
	 * 
	 * @param realCoord
	 *            - the coord referred to normal system
	 * @return the coord referred to grid system
	 * */
	public Coord toGridCoord(Coord realCoord) {
		int x = realCoord.getX() + smallX - 1;
		int y = 1 + greatY - realCoord.getY();
		return new Coord(x, y);
	}

	/**
	 * Convert a coord referred to grid system to coords referred to normal
	 * system.
	 * 
	 * @param gridCoord
	 *            - the coord referred to grid system.
	 * @return the coord referred to normal system.
	 * */
	public Coord toRealCoord(Coord gridCoord) {
		int x = gridCoord.getX() - smallX + 1;
		int y = 1 + greatY - gridCoord.getY();
		return new Coord(x, y);
	}

	/**
	 * Returns the dimension of grid.
	 * */
	public Dimension getDimension() {
		int x = INITIAL_GRID_DIMENSION - smallX + greatX;
		int y = INITIAL_GRID_DIMENSION - smallY + greatY;
		return new Dimension(x, y);
	}
}