package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {
    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NoteDataBase","postgres", "");

    public DataBaseHandler() throws SQLException {
    }

    public static boolean addDataBase(Connection connection, Note note) throws SQLException {
        String insertQuery = "INSERT INTO note(data, time, note ) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery);

        statement.setDate(1, note.date);
        statement.setTime(2,note.time);
        statement.setString(3,note.note);

        int count = statement.executeUpdate();
        return count > 0;
    }

    public static List<Note> getNote(Connection connection, Date date) throws SQLException {
        Note note = new Note();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM note WHERE data = ?");
        statement.setDate(1, date);
        ResultSet results = statement.executeQuery();
        if (!results.first())
            return null;

        List<Note> list =new ArrayList<>();
        while (results.next()){
            Note note1 = new Note();
            note1.date = results.getDate(1);
            note1.time = results.getTime(2);
            note1.note = results.getString(3);

            list.add(note1);
        }
        return list;
    }
}
