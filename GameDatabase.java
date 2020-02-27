package edu.stanford.cs108.bunnyworld;

import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;
//import javax.sql.rowset.serial.SerialBlob;

public class GameDatabase {

    private SQLiteDatabase db;

    public void initDatabase() {

        String clearStr = "DROP TABLE IF EXISTS games;";
        //db.execSQL(clearStr);

        String setupStr = "CREATE TABLE games ("
                + "game_num INTEGER, "
                + "game_data BLOB, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        System.err.println(setupStr);
        //db.execSQL(setupStr);
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





        // This method is not working because it cannot resolve SerialBlob
        /*
        //Blob testBlob = org.hibernate.Hibernate.createBlob(bytes);
        try {
            FileOutputStream fileOutStream = new FileOutputStream(new File("game.txt"));
            ObjectOutputStream objectOutStream = new ObjectOutputStream(fileOutStream);

            // Write object to file
            objectOutStream.writeObject(game);

            objectOutStream.close();
            fileOutStream.close();

            Scanner game_scanner = new Scanner(new BufferedReader(new FileReader("game.txt")));

            Blob game_blob = null;
            while (game_scanner.hasNext()) {
                byte[] game_byte_array = game_scanner.nextLine().getBytes();
                game_blob = new SerialBlob(game_byte_array);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

        */
    }

    /*
    public Game getGame(int gameNum) {

    }

     */

}
