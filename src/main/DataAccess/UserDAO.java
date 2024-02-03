package DataAccess;
import Models.User;
import dataAccess.DataAccessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Data access object class that provides methods to manage the users in the DB.
 */
public class UserDAO {
    /**
     * Collection of users that will be returned in findAllUsers method.
     */
    static Collection<User> users;
    static {users = new ArrayList<>();}

    /**
     * Inserts new user into DB.
     * @param user User that will be inserted into DB.
     * @throws DataAccessException Thrown if user already exists in DB.
     */
    public void insertUser(User user) throws DataAccessException {
        User possibleExistingUser = findUser(user.getUsername());
        if (possibleExistingUser != null) {
            throw new DataAccessException("Error: already taken");
        } else if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new DataAccessException("Error: bad request");
        }
        else {
            users.add(user);
            var DbConnection = new Database().getConnection();
            try {
                var preparedStatement = DbConnection.prepareStatement("INSERT INTO Users (Username, Password, Email) VALUES(?, ?, ?)", RETURN_GENERATED_KEYS);
                String username = user.getUsername();
                String password = user.getPassword();
                String email = user.getEmail();

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Finds specific user in the DB.
     * @param username User being searched for.
     * @return Returns user if it matches a user found in DB.
     * @throws DataAccessException Thrown if user is not found in DB.
     */
    public User findUser(String username)  throws DataAccessException {
        try {
            var DbConnection = new Database().getConnection();
            DbConnection.setCatalog("chess");

            String query = "SELECT * FROM Users WHERE Username = ?";
            var preparedStatement = DbConnection.prepareStatement(query, RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            var userSearch = preparedStatement.executeQuery();
            if (userSearch.next()) {
                User user = new User();
                user.setUsername(userSearch.getString(1));
                user.setPassword(userSearch.getString(2));
                user.setEmail(userSearch.getString(3));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    static public void clear() throws DataAccessException {
        var DbConnection = new Database().getConnection();
        try {
            String query = "DELETE FROM Users";
            var preparedStatement = DbConnection.prepareStatement(query, RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            DbConnection.close();
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }
}