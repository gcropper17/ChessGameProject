package Services;
import DataAccess.AuthDAO;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.User;
import Requests.RegisterRequest;
import Results.RegisterResult;
import dataAccess.DataAccessException;

import java.sql.SQLException;

/**
 * Class that determines if registering new user is possible.
 */
public class RegisterService {
    /**
     * Head method to determine if registering is possible.
     * @param r Request to register new user.
     * @return Returns successful or unsuccessful result.
     */
    public RegisterResult register(RegisterRequest r) {
        AuthToken newAuthToken = new AuthToken();
        User newUser = new User();
        RegisterResult result = new RegisterResult();
        UserDAO userObject = new UserDAO();
        AuthDAO newAuthDAO = new AuthDAO();

        newUser.setUsername(r.getUsername());
        newUser.setPassword(r.getPassword());
        newUser.setEmail(r.getEmail());
        try {
            userObject.insertUser(newUser);
            newAuthToken.setUsername(newUser.getUsername());
            newAuthToken.setAndGenerateAuthToken();
            newAuthDAO.insertAuthToken(newAuthToken);

            result.setUsername(newUser.getUsername());
            result.setAuthToken(newAuthToken.getAuthToken());
            return result;
        } catch (DataAccessException e) {
            result.setMessage(e.getMessage());
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}