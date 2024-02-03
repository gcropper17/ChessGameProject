package DataAccess;
import Models.AuthToken;
import dataAccess.DataAccessException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Data access object class that provides methods to manage the authTokens in the DB.
 */
public class AuthDAO {
    static Collection<AuthToken> tokens;
    static {
        tokens = new ArrayList<>();
    }

    public static Collection<AuthToken> getTokens() {
        return tokens;
    }

    /**
     * creates new authToken in DB.
     * @param a token that will be added.
     * @throws DataAccessException thrown if token already exists.
     */
     public void insertAuthToken(AuthToken a) throws DataAccessException, SQLException {
         tokens.add(a);
         var DbConnection = Database.getConnection();
         try {
             var preparedStatement = DbConnection.prepareStatement("INSERT INTO AuthTokens (AuthToken, Username) VALUES(?, ?)", RETURN_GENERATED_KEYS);
             String username = a.getUsername();
             String authToken = a.getAuthToken();
             preparedStatement.setString(1, authToken);
             preparedStatement.setString(2, username);
             preparedStatement.executeUpdate();

         } catch (SQLException e) {
             DbConnection.close();
             throw new RuntimeException(e);
         } finally {
             Database.returnConnection(DbConnection);
         }
     }
         /**
     * Check to see if Authtoken 'a' is in the DB. Will return 'a' if found.
     * @param token takes in an Authtoken type that will be used to see if it is in the DB.
     * @throws DataAccessException If parameter 'a' is not found in the DB, exception is thrown.
     */
    static public AuthToken findAuthToken(String token) throws DataAccessException {
        try {
            var DbConnection = Database.getConnection();
            try {
                String query = "SELECT * FROM AuthTokens WHERE AuthToken = ?";
                var preparedStatement = DbConnection.prepareStatement(query);
                preparedStatement.setString(1, token);
                var authTokenSearch = preparedStatement.executeQuery();
                if (authTokenSearch.next()) {
                    AuthToken authToken = new AuthToken();
                    authToken.setAuthToken(authTokenSearch.getString(1));
                    authToken.setUsername(authTokenSearch.getString(2));
                    DbConnection.close();
                    return authToken;
                } else {
                    throw new DataAccessException("Error: not Found");
                }

            } catch (DataAccessException | SQLException e) {
                DbConnection.close();
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (SQLException e){
            throw new DataAccessException("Error: unauthorized");
        }
    }


    /**
     * clears all authTokens from DB.
     */
    static public void clear() throws DataAccessException {
        var DbConnection = new Database().getConnection();
        try {
            String query = "DELETE FROM AuthTokens";
            var preparedStatement = DbConnection.prepareStatement(query, RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            DbConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tokens.clear();}

    /**
     * Removes specific authToken from DB.
     * @param a Authtoken to be removed from DB.
     * @throws DataAccessException Thrown if Authtoken 'a' is not found in DB.
     */
    public void removeAuthToken(AuthToken a) throws DataAccessException{
        var DbConnection = new Database().getConnection();

        try {
            AuthToken authToken = findAuthToken(a.getAuthToken());
            String query = "DELETE FROM AuthTokens WHERE authToken = ?";
            var preparedStatement = DbConnection.prepareStatement(query, RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, a.getAuthToken());
            preparedStatement.executeUpdate();
            DbConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}