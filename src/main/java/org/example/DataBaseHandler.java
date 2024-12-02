package org.example;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler {
    public static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/NoteDataBase","postgres", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataBaseHandler() throws SQLException {
    }

    public static boolean addDataBase(Note note) throws SQLException {
        String insertQuery = "INSERT INTO note(data, note ) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery);

        statement.setObject(1, note.dateTime);
        statement.setString(2, note.note);

        int count = statement.executeUpdate();
        return count > 0;
    }

    public static List<Note> getNote(Date date) throws SQLException {
        Note note = new Note();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM note WHERE data = ?");
        statement.setDate(1, date);
        ResultSet results = statement.executeQuery();
        if (!results.first())
            return null;

        List<Note> list =new ArrayList<>();
        while (results.next()){
            Note note1 = new Note();
            note1.dateTime = results.getObject(1, LocalDateTime.class);
            note1.note = results.getString(2);

            list.add(note1);
        }
        return list;
    }
}
