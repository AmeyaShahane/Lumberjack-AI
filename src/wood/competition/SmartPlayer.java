package wood.competition;

import wood.game.TurnAction;
import wood.item.InventoryItem;
import wood.strategy.PlayerBoardView;
import wood.strategy.WoodPlayerStrategy;
import wood.tiles.TileType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartPlayer implements WoodPlayerStrategy {

    private int playerTraversal = 200; // Experimentally determined frequency of turns to wait before cutting trees
    private final int SEEDS_TO_PLANT = 5; // Experimentally determined number of seeds to first plant
    private final int TREES_TO_CUT = 5; // Experimentally determined number of seeds to first plant

    private int myBoardSize; // local variable of passed in board state
    private boolean redPlayer = false; // boolean flag for red player
    private boolean bluePlayer = false; // boolean flag for blue player
    private Point startLocation; // startLocation signified as point
    private int playerInventorySize; // player inventory size
    private Point seedPoint; // seed point added to ArrayList as per traversal
    private int plantedSeeds = 0; // initial count of planted seeds
    private int treesCut = 0; // initial count of trees cut

    private int playerTurns = 0; // initial count of player turns
    private int treeRunCounter = 0; // initial count of tree run counter

    private boolean needToPlant = false; // initial boolean flag for need to plant
    List<Point> mySeedLocations = new ArrayList<>(); // ArrayList of seed locations
    List<Point> myTreeLocations = new ArrayList<>(); // ArrayList of tree locations

    public int testSeedsPlanted = 0; // public variable for test case counting seeds planted
    public boolean testNeedToPlant = false; // public boolean variable for asserting if need to plant
    public int testTreesCut = 0; // public int variable for test case asserting trees cut

    /**
     *
     * @param boardSize The length and width of the square game board
     * @param maxInventorySize The maximum number of items that your player can carry at one time
     * @param winningScore The first player to reach this score wins the round
     * @param startTileLocation A Point representing your starting location in (x, y) coordinates
     *                          (0, 0) is the bottom left and (boardSize - 1, boardSize - 1) is the top right
     * @param isRedPlayer True if this strategy is the red player, false otherwise
     * @param random A random number generator, if your strategy needs random numbers you should use this.
     */
    @Override
    public void initialize(int boardSize, int maxInventorySize, int winningScore, Point startTileLocation, boolean isRedPlayer, Random random) {
        myBoardSize = boardSize;
        if (isRedPlayer) {
            redPlayer = true;
        }
        if (!(isRedPlayer)) {
            bluePlayer = true;
        }
        startLocation = startTileLocation;
        playerInventorySize = maxInventorySize;
    }

    /**
     *
     * @param boardView A PlayerBoardView object representing all the information about the board and the other player
     *                  that your strategy is allowed to access
     * @param isRedTurn For use when two players attempt to move to the same spot on the same turn
     *                  If true: The red player will move to the spot, and the blue player will do nothing
     *                  If false: The blue player will move to the spot, and the red player will do nothing
     * @return
     */
    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, boolean isRedTurn) {

        // Checks to see if red player
        if (redPlayer) {
            if (needToPlant) {
                needToPlant = false;
                return TurnAction.PLANT_SEED;
            }
            // gets seeds for red player
            getSeedsRed(boardView);

            // Ensures player requires 5 amount of seeds to be planted with locations in an ArrayList
            if (plantedSeeds < SEEDS_TO_PLANT) {
                if (moveToLocation(boardView.getYourLocation(), mySeedLocations.get(0)) != null) {
                    return moveToLocation(boardView.getYourLocation(), mySeedLocations.get(0));
                } else {
                    myTreeLocations.add(mySeedLocations.get(0));
                    mySeedLocations.remove(0);
                    plantedSeeds++;
                    needToPlant = true;
                    return TurnAction.PICK_UP;

                }
            }

            // Ensures that player waits 200 turns until cutting trees
            if (playerTurns < playerTraversal) {
                playerTurns++;
                return null;
            }

            // Ensures that player cuts 5 trees and uses ArrayList of size 15 with tree locations
            if (treesCut < TREES_TO_CUT) {
                if (moveToLocation(boardView.getYourLocation(), myTreeLocations.get(0)) != null) {
                    return moveToLocation(boardView.getYourLocation(), myTreeLocations.get(0));
                } else {
                    treesCut++;
                    myTreeLocations.remove(0);
                    return TurnAction.CUT_TREE;
                }
            }

            // Ensures that player goes to starting location and then resets all required flags and variables
            if (moveToLocation(boardView.getYourLocation(), startLocation) != null) {
                return moveToLocation(boardView.getYourLocation(), startLocation);
            } else {
                plantedSeeds = 0;
                playerTurns = 0;
                treesCut = 0;
                treeRunCounter = 0;
                mySeedLocations = new ArrayList<>();
                myTreeLocations = new ArrayList<>();
            }
        }

        // Checks to see if blue player
        if (bluePlayer) {
            if (needToPlant) {
                needToPlant = false;
                return TurnAction.PLANT_SEED;
            }

            // gets seeds for blue player
            getSeedsBlue(boardView);

            // Ensures player requires 5 amount of seeds to be planted with locations in an ArrayList
            if (plantedSeeds < SEEDS_TO_PLANT) {
                if (moveToLocation(boardView.getYourLocation(), mySeedLocations.get(0)) != null) {
                    return moveToLocation(boardView.getYourLocation(), mySeedLocations.get(0));
                } else {
                    myTreeLocations.add(mySeedLocations.get(0));
                    mySeedLocations.remove(0);
                    plantedSeeds++;
                    needToPlant = true;
                    return TurnAction.PICK_UP;
                }
            }

            // Ensures that player waits 200 turns until cutting trees
            if (playerTurns < playerTraversal) {
                playerTurns++;
                return null;
            }

            // Ensures that player cuts 5 trees and uses ArrayList of size 15 with tree locations
            if (treesCut < TREES_TO_CUT) {
                if (moveToLocation(boardView.getYourLocation(), myTreeLocations.get(0)) != null) {
                    return moveToLocation(boardView.getYourLocation(), myTreeLocations.get(0));
                } else {
                    treesCut++;
                    myTreeLocations.remove(0);
                    return TurnAction.CUT_TREE;
                }
            }

            // Ensures that player goes to starting location and then resets all required flags and variables
            if (moveToLocation(boardView.getYourLocation(), startLocation) != null) {
                return moveToLocation(boardView.getYourLocation(), startLocation);
            } else {
                plantedSeeds = 0;
                playerTurns = 0;
                treesCut = 0;
                treeRunCounter = 0;
                mySeedLocations = new ArrayList<>();
                myTreeLocations = new ArrayList<>();
            }
        }
        return null;
    }


    /**
     * Adds seed point for blue player
     * @param boardView
     */
    private void getSeedsBlue(PlayerBoardView boardView) {
        for (int i = myBoardSize - 1; i > 0; i--) { // row
            for (int j = myBoardSize - 1; j > 0; j--) { // column
                if (boardView.getTileTypeAtLocation(i, j).equals(TileType.SEED)) {
                    seedPoint = new Point(i, j);

                    mySeedLocations.add(seedPoint);
                }
            }
        }
    }

    /**
     * Adds seed point for red player
     * @param boardView
     */
    private void getSeedsRed(PlayerBoardView boardView) {
        for (int i = 0; i < myBoardSize; i++) { // row
            for (int j = 0; j < myBoardSize; j++) { // column
                if (boardView.getTileTypeAtLocation(i, j).equals(TileType.SEED)) {
                    seedPoint = new Point(i, j);

                    mySeedLocations.add(seedPoint);
                }
            }
        }
    }

    /**
     * Ensures player moves to respective location
     * @param currentLocation
     * @param destination
     * @return
     */
    private TurnAction moveToLocation(Point currentLocation, Point destination) {
        if (currentLocation.x < destination.x) {
            return TurnAction.MOVE_RIGHT;
        }

        if (currentLocation.x > destination.x) {
            return TurnAction.MOVE_LEFT;
        }

        if (currentLocation.y < destination.y) {
            return TurnAction.MOVE_UP;
        }

        if (currentLocation.y > destination.y) {
            return TurnAction.MOVE_DOWN;
        }
        return null;
    }

    /**
     *
     * @param itemReceived The item received from the player's TurnAction on their last turn
     */
    @Override
    public void receiveItem(InventoryItem itemReceived) {

    }

    /**
     *
     * @return player strategy name
     */
    @Override
    public String getName() {
        return "SmartPlayer";
    }

    /**
     *
     * @param pointsScored The total number of points this strategy scored
     * @param opponentPointsScored The total number of points the opponent's strategy scored
     */
    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {
    }

    /**
     * Test Suite Methods
     */

    // Ensures that seed is planted
    public TurnAction plantSeed() {
        if (testNeedToPlant) {
            testNeedToPlant = false;
            return TurnAction.PLANT_SEED;
        }
        return null;
    }

    // Ensures that seed is picked up
    public String pickUpSeed(Point currentLocation, Point destination) {
        if (testSeedsPlanted < SEEDS_TO_PLANT) {
            if (currentLocation == destination) {
                return "Picked Up Seed";
            } else {
                return "Still need to traverse";
            }
        }
        return null;
    }

    // Ensures that player has moved to location from current location
    public TurnAction moveToTestLocation(Point currentLocation, Point destination) {
        if (currentLocation.x < destination.x) {
            return TurnAction.MOVE_RIGHT;
        }

        if (currentLocation.x > destination.x) {
            return TurnAction.MOVE_LEFT;
        }

        if (currentLocation.y < destination.y) {
            return TurnAction.MOVE_UP;
        }

        if (currentLocation.y > destination.y) {
            return TurnAction.MOVE_DOWN;
        }
        return null;
    }

    // Ensures that tree is cut at a certain point
    public String treeCut(Point currentLocation, Point destination) {
        if (testTreesCut < 5) {
            if (currentLocation == destination) {
                testTreesCut++;
                return "Tree has been cut";

            }
        }
        return null;
    }

    // Ensures that blue player has received seeds
    public String getSeedsTestBlue(PlayerBoardView boardView) {
        for (int i = myBoardSize - 1; i > 0; i--) { // row
            for (int j = myBoardSize - 1; j > 0; j--) { // column
                if (boardView.getTileTypeAtLocation(i, j).equals(TileType.SEED)) {
                    seedPoint = new Point(i, j);
                    mySeedLocations.add(seedPoint);
                    return "Seeds have been initialized for Blue Player" + " " + seedPoint;
                }
            }
        }
        return null;
    }

    // Ensures that red player has received seeds
    public String getSeedsTestRed(PlayerBoardView boardView) {
        for (int i = 0; i < myBoardSize; i++) { // row
            for (int j = 0; j < myBoardSize; j++) { // column
                if (boardView.getTileTypeAtLocation(i, j).equals(TileType.SEED)) {
                    seedPoint = new Point(i, j);

                    mySeedLocations.add(seedPoint);
                    return "Seeds have been initialized for Red Player" + " " + seedPoint;
                }
            }
        }
        return null;
    }
}

