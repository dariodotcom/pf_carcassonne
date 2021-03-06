package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing entities (Road and Cities) and shared methods.
 * */
public abstract class Entity {

	/* Factory method */
	/**
	 * Creates a new instance of the Entity implementation corresponding to the
	 * given type.
	 * 
	 * @param type
	 *            - the type of the entity to create
	 * @return a new instance of a Entity of the given type.
	 * */
	public static Entity createByType(EntityType type) {
		switch (type) {
		case C:
			return new City();
		case S:
			return new Road();
		default:
			return null;
		}
	}

	private List<Side> members;

	private boolean completed;
	private boolean hasFollowers;

	/**
	 * Entity constructor. Creates a new instance of class Entity.
	 */
	protected Entity() {
		members = new ArrayList<Side>();
	}

	/**
	 * Adds a member to this entity.
	 */
	public void addMember(Side side) {
		if (!members.contains(side)) {
			members.add(side);
		}
	}

	/**
	 * Checks if an entity is completed.
	 * 
	 * @return true if the entity is completed, false otherwise
	 * */
	public boolean isComplete() {
		List<Side> checkedSides = new ArrayList<Side>();

		if (completed) {
			return true;
		}

		for (Side m : members) {
			if (checkedSides.contains(m)) {
				continue;
			}
			Side opposite = m.getOppositeSide();
			if (opposite == null) {
				return false;
			} else {
				checkedSides.add(opposite);
			}
		}
		completed = true;
		return true;
	}

	/**
	 * Merges a given entity into this one.
	 */
	public Entity enclose(Entity otherEntity) {
		if (otherEntity.getMembers().size() > this.members.size()) {
			return otherEntity.enclose(this);
		} else {
			for (Side s : otherEntity.getMembers()) {
				s.setEntity(this);
				this.addMember(s);
			}
			if (!otherEntity.acceptFollowers()) {
				hasFollowers = true;
			}
			return this;
		}
	}

	/**
	 * Check if a follower can be put on a city.
	 */
	public boolean acceptFollowers() {
		if (hasFollowers) {
			return false;
		}
		for (Side s : members) {
			if (s.getFollower() != null) {
				hasFollowers = true;
				return false;
			}
		}
		return true;
	}

	/**
	 * Counts followers placed on an entity.
	 * 
	 * @return an array in which counters are placed in players' order
	 * */
	public int[] countFollowers(int numPlayers) {
		int[] counter = new int[numPlayers];

		for (int i = 0; i < numPlayers; i++) {
			counter[i] = 0;
		}
		for (Side s : members) {
			PlayerColor follower = s.getFollower();
			if (follower == null) {
				continue;
			}
			int index = PlayerColor.indexOf(follower);
			counter[index]++;
		}

		return counter;
	}

	/**
	 * Removes the followers of a given color from entity; if given color is
	 * null, all followers are removed.
	 * 
	 * @param toRemove
	 *            - the color to remove
	 */
	public List<Tile> removeFollowers(PlayerColor toRemove) {
		hasFollowers = false;
		ArrayList<Tile> updatedCards = new ArrayList<Tile>();
		for (Side s : members) {
			PlayerColor follower = s.getFollower();
			if (follower != null && (toRemove == null || follower == toRemove)) {
				s.setFollower(null);
				Tile c = s.getOwnerCard();
				if (!updatedCards.contains(c)) {
					updatedCards.add(c);
				}
			}
		}
		return updatedCards;
	}

	/* Abstract methods. */

	/**
	 * Gives the type of this entity.
	 * 
	 * @return the EntityType of this entity;
	 * */
	public abstract EntityType getType();

	/**
	 * Returns the score given to owners by this entity;
	 * */
	public abstract int getScore();

	/**
	 * Obtains the list of this entity's members.
	 */
	protected List<Side> getMembers() {
		return members;
	}
}
