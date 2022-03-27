package com.example.knygos2.model;

import com.example.knygos2.controler.DashboardController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    //---------------------Kuriame įrašą---------------------
    public static void create(Book book) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String querry = "INSERT INTO `knygos`(`title`, `page`, `category`, `autor`, `summary`) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");

            PreparedStatement preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getPage());
            preparedStatement.setString(3, book.getCategory());
            preparedStatement.setString(4, book.getAutor());
            preparedStatement.setString(5, book.getSummary());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------Įrašo paieška pagal name---------------------
    public static List<Book> searchByName(String name) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String querry = "";
        if (name.isEmpty()) {
            querry = "SELECT * FROM `knygos`";
        } else {
            querry = "SELECT * FROM `knygos` WHERE `title` LIKE '%" + name + "%'";
        }

        ArrayList<Book> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(querry);
//            if(!name.isEmpty()){
//                preparedStatement.setString(1, name);
//            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) { //Kol turime sarase elementus
                list.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getInt("page"),
                        resultSet.getString("category"),
                        resultSet.getString("autor"),
                        resultSet.getString("summary")
                ));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //---------------------Atnaujiname įrašą---------------------
    public static void update(Book book) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String update = "UPDATE `knygos` SET `title`= ?,`page`= ?,`category`= ?,`autor`= ?,`summary`= ? WHERE `id` = ?";
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getPage());
            preparedStatement.setString(3, book.getCategory());
            preparedStatement.setString(4, book.getAutor());
            preparedStatement.setString(5, book.getSummary());
            preparedStatement.setInt(6, book.getId());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            System.out.println("Pavyko atnaujinti įrašą");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nepavyko atnaujinti įrašą");
        }
    }

    //---------------------Triname įrašą---------------------
    public static void deleteById(int id) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String delete = "DELETE FROM knygos WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");

            PreparedStatement preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            System.out.println("Pavyko ištrinti įrašą");
        } catch (SQLException e) {
            e.printStackTrace();

            System.out.println("Įrašo ištrinti nepavyko");
        }
    }


    //---------------------Įrašo paieška pagal id---------------------
    public static Book searchById(String id) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db";
        String querry = "SELECT * FROM `knygos` WHERE `id` = ?";

        ArrayList<Book> list = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement(querry);
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) { //Kol turime sarase elementus
                list.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getInt("page"),
                        resultSet.getString("category"),
                        resultSet.getString("autor"),
                        resultSet.getString("summary")
                ));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Ieskome pagal pirma sarase esanti id, nes id unikalus
            return list.get(0);
        }
        // Nepavyko rasti pagal id
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


}


