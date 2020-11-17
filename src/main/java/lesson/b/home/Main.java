package lesson.b.home;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pstmt;

    public static void main(String[] args) {

        try {
            connect();

            createTable("NewTable", "Name", "Score");

            insertInto("NewTable", "Name", "Score", "This_is_Boby", 987);
            insertInto("NewTable", "Name", "Score", "This_is_NotBoby", 231);

            System.out.println(selectAll("NewTable"));
            System.out.println(selectFromColumn("NewTable", "ID", "3"));
            System.out.println(selectFromColumn("NewTable", "Name", "This_is_NotBoby"));

            System.out.println(deleteRowWhere("NewTable", "Name", "This_is_NotBoby"));
            System.out.println("Deleted rows: " + deleteAll("NewTable"));

            System.out.println(deleteTable("NewTable"));

            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        stmt = connection.createStatement();
    }

    private static void disconnect() throws SQLException {
        connection.close();
    }

    private static void createTable(String nameTable, String nameColumn1, String nameColumn2) throws SQLException {     //создание таблицы
        String sQuery = "CREATE TABLE IF NOT EXISTS " + nameTable + " (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                nameColumn1 + " TEXT NOT NULL, " + nameColumn2 + " INTEGER NOT NULL);";
        int res = stmt.executeUpdate(sQuery);
    }
                                                                                                                //добавление записи в таблицу
    private static void insertInto(String nameTable, String nameColumn1, String nameColumn2, String valColumn1, int valColumn2) throws SQLException {
        String sQueryInsert = "INSERT INTO " + nameTable + " (" + nameColumn1 + ", " + nameColumn2
                + ") VALUES ('" + valColumn1 + "', '" + valColumn2 + "')";
        stmt.executeUpdate(sQueryInsert);
    }


    private static String selectAll (String nameTable) throws SQLException {        //получение всех записей в таблице
        String sQuery = "SELECT * FROM " + nameTable;
        ResultSet result = stmt.executeQuery(sQuery);
        int columns = result.getMetaData().getColumnCount();

        String strResult = new String();

        while (result.next()){
            for (int j = 0; j < (columns - 1); j++) {
                strResult += result.getMetaData().getColumnName(j+1) + ": " + result.getString((j + 1)) + ", ";
            }
            strResult += result.getMetaData().getColumnName(columns) + ": " + result.getString((columns)) + ";\n";
        }

        return strResult;
    }

    private static String selectFromColumn (String nameTable, String nameColumn, String valColumn) throws SQLException {  //получение записи по значению в столбце
        String sQuery = "SELECT * FROM " + nameTable + " WHERE " + nameColumn + " = '" + valColumn + "'";
        ResultSet result = stmt.executeQuery(sQuery);
        int columns = result.getMetaData().getColumnCount();

        String strResult = new String();

        while (result.next()){
            for (int j = 0; j < (columns - 1); j++) {
                strResult += result.getMetaData().getColumnName(j+1) + ": " + result.getString((j + 1)) + ", ";
            }
            strResult += result.getMetaData().getColumnName(columns) + ": " + result.getString((columns)) + ";\n";
        }

        return strResult;
    }

    private static String deleteRowWhere (String nameTable, String nameColumn, String valColumn) throws SQLException {        //удаление записи
        String sQuery = "DELETE FROM " + nameTable + " WHERE " + nameColumn + " = '" + valColumn + "'";
        int result = stmt.executeUpdate(sQuery);

        return "Row FROM " + nameTable + " WHERE " + nameColumn + " = '" + valColumn + "' is deleted";
    }

    private static int deleteAll (String nameTable) throws SQLException {        //удаление всех записей в таблице
        String sQuery = "DELETE FROM " + nameTable;
        int result = stmt.executeUpdate(sQuery);
        return result;
    }

    private static String deleteTable (String nameTable) throws SQLException {        //удаление всей таблицы
        String sQuery = "DROP TABLE " + nameTable;
        int result = stmt.executeUpdate(sQuery);
        return "Table " + nameTable + " is deleted";
    }


}


