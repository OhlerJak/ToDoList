package database;


import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private static Database instance =null;

    public static Database getInstance() {
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }


    private Connection c;

    public Database()  {
        try {
            c = DriverManager.getConnection("jdbc:derby://localhost:1527/ToDoDB;create=true","app","password");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{
            // Create DB
            Statement s = c.createStatement();

            s.executeUpdate("CREATE TABLE TODO(id int primary key NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), beschreibung varchar(512) not null)");
            System.out.println("Executed!");
        }catch (SQLException e){
            System.out.println("Keine neuen Tabellen erzeugt! (existieren schon)");
        }
    }

    public Connection getConnection() {
        return c;
    }

    public void saveToDo(String ToDo) throws SQLException {
        PreparedStatement ps = c.prepareStatement("insert into ToDo( beschreibung) values (?)");
        ps.setString(1, ToDo);
        ps.executeUpdate();
    }

    public void saveData(List<String> todos) throws SQLException {
        for (String todo : todos) {
            saveToDo(todo);
        }
    }


    public List<String> getallData() throws SQLException{
        List<String> todoList = new LinkedList<>();
        PreparedStatement ps = c.prepareStatement("SELECT beschreibung FROM ToDo ");
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            todoList.add(rs.getString(1));
        }

        return todoList;
    }
}
