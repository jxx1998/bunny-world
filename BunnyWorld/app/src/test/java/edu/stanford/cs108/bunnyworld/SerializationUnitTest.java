package edu.stanford.cs108.bunnyworld;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SerializationUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGameSerialization() {
        Game game = new Game();
        Page page = new Page("dummyPage");
        Shape shape = new ShapeBuilder().name("dummyShape").coordinates(10.0f, 10.0f, 10.0f, 10.0f).buildShape();
        page.addShape(shape);
        game.addPage(page);
        byte[] gameSerialized = game.serialize();
        System.out.println(gameSerialized);
        Game deserialized_game = Game.deserialize(gameSerialized);
        assertEquals(deserialized_game.pages.get(0).name, "dummyPage");
        assertEquals(deserialized_game.pages.get(0).shapes.get(0).name, "dummyShape");
    }
}