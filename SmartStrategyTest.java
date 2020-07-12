import org.junit.Before;


import org.junit.Test;
import wood.competition.SmartPlayer;
import wood.game.TurnAction;
import wood.strategy.PlayerBoardView;
import wood.tiles.TileType;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static wood.game.TurnAction.MOVE_UP;
import static wood.game.TurnAction.PLANT_SEED;

public class SmartStrategyTest {

    private final int boardDimensions = 10;
    TileType[][] gameTiles = new TileType[boardDimensions][boardDimensions];


    private Point playerRedLocation = new Point(3, 3); // example player red location
    private Point playerBlueLocation = new Point(2, 4); // example player blue location
    private Point alternateRedLocation = new Point(0, 1); // alternate player red location
    private Point alternateBlueLocation = new Point(2, 1); // alternate player blue location

    TileType[][] gameTile = // standard game tile
            {{TileType.SEED, TileType.TREE, TileType.TREE, TileType.TREE, TileType.START},
                    {TileType.SEED, TileType.SEED, TileType.SEED, TileType.EMPTY, TileType.SEED},
                    {TileType.SEED, TileType.TREE, TileType.SEED, TileType.TREE, TileType.SEED},
                    {TileType.SEED, TileType.SEED, TileType.SEED, TileType.EMPTY, TileType.SEED},
                    {TileType.START, TileType.TREE, TileType.SEED, TileType.EMPTY, TileType.SEED}};

    PlayerBoardView boardView = new PlayerBoardView(gameTile, playerRedLocation, playerBlueLocation
            , 5, 0);

    // Ensures that player strategy name is valid
    @Test
    public void assertStrategyName() {
        SmartPlayer x = new SmartPlayer();
        assertEquals("SmartPlayer", x.getName());
    }

    // Checks to see if red player seeds are initialized
    @Test
    public void assertRedSeedsInitialization() {
        Point redPlayerPoint = new Point(0, 0);
        SmartPlayer redPlayer = new SmartPlayer();
        redPlayer.initialize(5, 5, 2000, redPlayerPoint,
                true, new Random(1));

        assertEquals(redPlayer.getSeedsTestRed(boardView), "Seeds have been " +
                "initialized for Red Player java.awt.Point[x=0,y=1]");
    }

    // Checks to see if blue player seeds are initialized
    @Test
    public void assertBlueSeedsInitialization() {
        Point bluePlayerPoint = new Point(4, 4);
        SmartPlayer bluePlayer = new SmartPlayer();
        bluePlayer.initialize(5, 5, 2000, bluePlayerPoint,
                false, new Random(7));
        assertEquals(bluePlayer.getSeedsTestBlue(boardView), "Seeds have been" +
                " initialized for Blue Player java.awt.Point[x=4,y=3]");
    }

    // Checks to see if red player moved to location
    @Test
    public void movedToTestRedLocation() {
        Point redPlayerPoint = new Point(0, 0);
        Point currentLocation = new Point(4, 5);
        Point destination = new Point(4, 5);
        SmartPlayer redPlayer = new SmartPlayer();
        redPlayer.initialize(5, 5, 2000, redPlayerPoint,
                true, new Random(1));
        assertEquals(redPlayer.moveToTestLocation(currentLocation, destination), null);
    }

    // Checks to see if blue player moved to location
    @Test
    public void movedToTestBlueLocation() {
        Point bluePlayerPoint = new Point(4, 4);
        Point currentLocation = new Point(4, 5);
        Point destination = new Point(4, 5);
        SmartPlayer bluePlayer = new SmartPlayer();
        bluePlayer.initialize(5, 5, 2000, bluePlayerPoint,
                true, new Random(1));
        assertEquals(bluePlayer.moveToTestLocation(currentLocation, destination), null);
    }

    // Checks to see if player needs to plant
    @Test
    public void assertNeedToPlant() {
        SmartPlayer x = new SmartPlayer();
        x.testNeedToPlant = true;
        assertEquals(x.plantSeed(), PLANT_SEED);
    }

    // Checks to see if seed is not picked up for red player
    @Test
    public void assertNotPickedUpSeedRed() {
        SmartPlayer redPlayer = new SmartPlayer();
        redPlayer.testSeedsPlanted = 0;
        Point redPlayerPoint = new Point(0, 0);
        Point currentLocation = new Point(2, 5);
        Point destination = new Point(3, 5);
        redPlayer.initialize(5, 5, 2000, redPlayerPoint,
                true, new Random(1));
        assertEquals(redPlayer.pickUpSeed(currentLocation, destination), "Still need to traverse");
    }

    // Checks to see if seed is not picked up for blue player
    @Test
    public void assertNotPickedUpSeedBlue() {
        SmartPlayer bluePlayer = new SmartPlayer();
        bluePlayer.testSeedsPlanted = 0;
        Point bluePlayerPoint = new Point(4, 4);
        Point currentLocation = new Point(2, 5);
        Point destination = new Point(3, 5);
        bluePlayer.initialize(5, 5, 2000, bluePlayerPoint,
                true, new Random(1));
        assertEquals(bluePlayer.pickUpSeed(currentLocation, destination), "Still need to traverse");
    }

    // Checks to if tree is not cut for red player
    @Test
    public void assertRedTreeIsNotCut() {
        Point bluePlayerPoint = new Point(4, 4);
        Point currentLocation = new Point(2, 5);
        Point destination = new Point(3, 5);
        SmartPlayer redPlayer = new SmartPlayer();
        redPlayer.initialize(5, 5, 2000, bluePlayerPoint,
                true, new Random(1));
        redPlayer.testTreesCut = 15;
        assertEquals(redPlayer.treeCut(currentLocation, destination), null);
    }

    // Checks to if tree is not cut for blue player
    @Test
    public void assertBlueTreeIsNotCut() {
        Point bluePlayerPoint = new Point(4, 4);
        Point currentLocation = new Point(2, 5);
        Point destination = new Point(3, 5);
        SmartPlayer bluePlayer = new SmartPlayer();
        bluePlayer.testTreesCut = 15;
        bluePlayer.initialize(5, 5, 2000, bluePlayerPoint,
                true, new Random(1));
        assertEquals(bluePlayer.treeCut(currentLocation, destination), null);
    }

}
