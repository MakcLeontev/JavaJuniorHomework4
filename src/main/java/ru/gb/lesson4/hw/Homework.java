package ru.gb.lesson4.hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.gb.lesson4.User;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Homework {

  /**
   * Задания необходимо выполнять на ЛЮБОЙ СУБД (postgres, mysql, sqllite, h2, ...)
   *
   * 1. С помощью JDBC выполнить:
   * 1.1 Создать таблицу book с колонками id bigint, name varchar, author varchar, ...
   * 1.2 Добавить в таблицу 10 книг
   * 1.3 Сделать запрос select from book where author = 'какое-то имя' и прочитать его с помощью ResultSet
   *
   * 2. С помощью JPA(Hibernate) выполнить:
   * 2.1 Описать сущность Book из пункта 1.1
   * 2.2 Создать Session и сохранить в таблицу 10 книг
   * 2.3 Выгрузить список книг какого-то автора
   *
   * 3.* Создать сущность Автор (id biging, name varchar), и в сущности Book сделать поле типа Author (OneToOne)
   * 3.1 * Выгрузить Список книг и убедиться, что поле author заполнено
   * 3.2 ** В классе Author создать поле List<Book>, которое описывает список всех книг этого автора. (OneToMany)
   */

  /**
   * ДЗ для преподавателя:
   * 1. Разобраться с jdbc:h2:file
   * 2. Подготовить расказ про PreparedStatement и SqlInjection
   */
  public static void main(String[] args) throws SQLException {
//      Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
      // 1.1 Создать таблицу book с колонками id bigint, name varchar, author varchar, ...
     // createTable(connection);
      // 1.2 Добавить в таблицу 10 книг
//      insertData(connection);
      // 1.3 Сделать запрос select from book where author = 'какое-то имя' и прочитать его с помощью ResultSet
      //executeSelect(connection);

      // 2. С помощью JPA(Hibernate) выполнить:
      // 2.1 Описать сущность Book из пункта 1.1
      // 2.2 Создать Session и сохранить в таблицу 10 книг
      final SessionFactory sessionFactory = new Configuration()
              .configure("hibernate.cfg.xml").buildSessionFactory();

//      try (Session session = sessionFactory.openSession()) {
//          session.beginTransaction();
//
//          List<Book> books = LongStream.rangeClosed(1, 10)
//                  .mapToObj(it -> new Book(it))
//                  .peek(it -> it.setName("Book #" + it.getId()))
//                  .peek(it -> it.setAuthor("Author #" + it.getId()))
//                  .peek(it -> session.persist(it))
//                  .collect(Collectors.toList());
//
//          session.getTransaction().commit();
//      }
      // 2.3 Выгрузить список книг какого-то автора

      try (Session session = sessionFactory.openSession()) {
          List<Book> books = session.createQuery("select b from Book b where author = 'Федор Достоевский'", Book.class)
                  .getResultList();

          System.out.println(books);
      }

  }
  public static void createTable(Connection connection){
      try(Statement statement = connection.createStatement();) {
          statement.execute("""
        create table if not exists book (
          id bigint,
          name varchar(255),
          author varchar(255)
        )
        """);
      }catch (SQLException e) {
          throw new RuntimeException(e);
      }
  }
  private static void insertData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
        insert into book(id, name, author) 
        values(1, 'Гарри Поттер и узник Азкабана', 'Джоан Роулинг'),
              (2, 'Мастер и Маргарита', 'Михаил Булгаков'),
              (3, 'Тихий Дон', 'Михаил Шолохов'),
              (4, 'Повелитель мух', 'Уильям Голдинг'),
              (5, 'Гроздья гнева', 'Джон Эрнст Стейнбек'),
              (6, 'На Западном фронте без перемен', 'Эрих Мария Ремарк'),
              (7, 'Сто лет одиночества', 'Габриэль Гарсиа Маркес'),
              (8, 'Преступление и наказание', 'Федор Достоевский'),
              (9, 'Жизнь взаймы', 'Эрих Мария Ремарк'),
              (10, 'Идиот', 'Федор Достоевский')""");
        }
    }
  private static void executeSelect(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, name, author  from book where author = 'Федор Достоевский'");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                System.out.println("id = " + id + ", name = " + name + ", author = " + author);
            }
        }
    }
}
