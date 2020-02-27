package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // MyExample ex = new MyExample();
        GameDatabase game_database = new GameDatabase();
        db = openOrCreateDatabase("GameDB", MODE_PRIVATE, null);
        initDatabase();

        try {
            testSave();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onChooseEditor(View view) {
        System.out.println("In the onChooseEditor method");
        Intent intent = new Intent(this, EditorActivity.class);
        System.out.println("After the intent creation");
        startActivity(intent);
    }

    private void initDatabase() {
        String clearStr = "DROP TABLE IF EXISTS games;";
        db.execSQL(clearStr);

        String setupStr = "CREATE TABLE games ("
                + "game_num INTEGER, "
                + "game_data BLOB, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        System.err.println(setupStr);
        db.execSQL(setupStr);
    }


    public byte[] gameToBytes(Game game) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(game);
        oos.flush();
        byte[] data = bos.toByteArray();
        return data;
    }

    // https://www.techiedelight.com/convert-byte-array-string-java/
    public String bytesToString(byte[] game_bytes) {
        String str = new String(game_bytes);
        return str;
    }

    public void saveGame(int gameNum, Game game) throws IOException {
        // Clear out database
        String deleteStr = "DELETE FROM games WHERE "
                + "game_num = " + Integer.toString(gameNum) + ";";

        db.execSQL(deleteStr);

        // This approach directly casts to byte array and then to string. We can see if this works
        byte[] game_byte_array = gameToBytes(game);
        String game_string = bytesToString(game_byte_array);

        // Command dynamically converts to blob
        String dataStr = "INSERT INTO games VALUES "
                + "(" + Integer.toString(gameNum)
                + ", " + game_string + ", NULL);";

        System.err.println(dataStr);
        db.execSQL(dataStr);
    }

    public void testSave() throws IOException {
        Game testGame = new Game();
        saveGame(1, testGame);
    }

}
