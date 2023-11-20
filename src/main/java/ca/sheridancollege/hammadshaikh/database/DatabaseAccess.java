package ca.sheridancollege.hammadshaikh.database;

import ca.sheridancollege.hammadshaikh.beans.Book;
import ca.sheridancollege.hammadshaikh.beans.Order;
import ca.sheridancollege.hammadshaikh.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseAccess {
    @Autowired
    protected NamedParameterJdbcTemplate jdbc;

    //User methods

    //Method to find a user account by email
    public User findUserAccount(String email) {
        MapSqlParameterSource namedParameters = new
                MapSqlParameterSource();
        String query = "SELECT * FROM sec_user where email = :email";
        namedParameters.addValue("email", email);
        try {
            return jdbc.queryForObject(query, namedParameters, new
                    BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException erdae) {
            return null;
        }
    }
    // Method to get User Roles for a specific User id
    public List<String> getRolesById(Long userId) {
        MapSqlParameterSource namedParameters = new
                MapSqlParameterSource();
        String query = "SELECT sec_role.roleName "
                + "FROM user_role, sec_role "
                + "WHERE user_role.roleId = sec_role.roleId "
                + "AND userId = :userId";
        namedParameters.addValue("userId", userId);
        return jdbc.queryForList(query, namedParameters,
                String.class);
    }
    public void insertUser(String username, String password) {
        // Adding named parameters
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", username);
        namedParameters.addValue("password", password);

        String query = "INSERT INTO sec_user(email, encryptedPassword, enabled) VALUES (:username, :password, 1)";  // Using the named parameter

        int rowsAffected = jdbc.update(query, namedParameters);

        /*  By default, assign USER role to new accounts, In my opinion,
            Admin roles should be applied manually by the administrator
            in the database
         */
        if (rowsAffected > 0){
            User user = findUserAccount(username);
            namedParameters.addValue("userId", user.getUserId());
            System.out.println("User inserted into database");
            String query2 = "INSERT INTO user_role(userId, roleId) VALUES (:userId, 1)";
            jdbc.update(query2, namedParameters);
            System.out.println("User assigned role");
        }

    }
    public List<User> getUserList() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM sec_user";
        return jdbc.query(query, namedParameters, new
                BeanPropertyRowMapper<User>(User.class));
    }
    public void insertAdmin(Long userId) {
        // Adding named parameters
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);

        String query = "INSERT INTO user_role(userId, roleId) VALUES (:userId, 3)";  // Using the named parameter

        int rowsAffected = jdbc.update(query, namedParameters);

        /*  By default, assign USER role to new accounts, In my opinion,
            Admin roles should be applied manually by the administrator
            in the database
         */
        if (rowsAffected > 0){
            System.out.println("User assigned role");
        }

    }
    public void removeAdmin(Long userId) {
        // Adding named parameters
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);

        String query = "DELETE FROM user_role WHERE userId = :userId AND roleId = 3";  // Using the named parameter

        int rowsAffected = jdbc.update(query, namedParameters);

        /*  By default, assign USER role to new accounts, In my opinion,
            Admin roles should be applied manually by the administrator
            in the database
         */
        if (rowsAffected > 0){
            System.out.println("User removed role");
        }

    }
    //Book Methods
    public void insertBook(Book book) {
        // Adding named parameters
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("isbn", book.getIsbn());
        namedParameters.addValue("title", book.getTitle());
        namedParameters.addValue("author", book.getAuthor());
        namedParameters.addValue("price", book.getPrice());
        namedParameters.addValue("description", book.getDescription());

        String query = "INSERT INTO book(title, author, price, description) VALUES (:title, :author, :price, :description)"; // Using the named parameter

        int rowsAffected = jdbc.update(query, namedParameters);

        if (rowsAffected > 0) System.out.println("Book inserted into database");

    }
    public void updateBook(Book updatedBook) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("isbn", updatedBook.getIsbn());
        namedParameters.addValue("title", updatedBook.getTitle());
        namedParameters.addValue("author", updatedBook.getAuthor());
        namedParameters.addValue("price", updatedBook.getPrice());
        namedParameters.addValue("description", updatedBook.getDescription());
        String query = "UPDATE book SET title = :title, author = :author, price = :price, description = :description WHERE isbn = :isbn";
        int rowsAffected = jdbc.update(query, namedParameters);
        if (rowsAffected > 0) {
            System.out.println("Updated book with ISBN " + updatedBook.getIsbn() + " in the database.");
        }
    }
    public List<Book> getBookList() {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM book";
        return jdbc.query(query, namedParameters, new
                BeanPropertyRowMapper<Book>(Book.class));
    }

    public void deleteBookByIsbn(Long isbn) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "DELETE FROM book WHERE isbn = :isbn";
        namedParameters.addValue("isbn", isbn);
        if (jdbc.update(query, namedParameters) > 0) {
            System.out.println("Deleted book " + isbn + " from the database.");
        }
    }
    public List<Book> getBookByIsbn(Long isbn) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        String query = "SELECT * FROM book WHERE isbn = :isbn";
        namedParameters.addValue("isbn", isbn);
        return jdbc.query(query, namedParameters, new
                BeanPropertyRowMapper<Book>(Book.class));
    }
    //Checkout function to place orders
    public void insertOrders(List<Order> orderList) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        for (Order order:orderList
             ) {
            namedParameters.addValue("isbn", order.getIsbn());
            namedParameters.addValue("quantity", order.getQuantity());
            namedParameters.addValue("owner", order.getUsername());
            namedParameters.addValue("total", order.getTotal());
            // Adding a named parameter

            String query = "INSERT INTO book_order(isbn, quantity, owner, total) VALUES (:isbn, :quantity, :owner, :total)"; // Using the named parameter

            int rowsAffected = jdbc.update(query, namedParameters);

            if (rowsAffected > 0) System.out.println("Order inserted into database");
        }

    }
}
