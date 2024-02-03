package Services;
import DataAccess.AuthDAO;
import DataAccess.UserDAO;
import Models.AuthToken;
import Models.User;
import Requests.LoginRequest;
import Results.LoginResult;
import dataAccess.DataAccessException;

import java.sql.SQLException;
import java.util.Objects;
/**
 * Class that determines if log in of user is possible.
 */
public class LoginService {
    /**
     * Head method that determines if log in of user is possible.
     * @param r Request to log in.
     * @return Returns successful or unsuccessful result.
     */
    public LoginResult login(LoginRequest r) {
        AuthToken newAuthToken = new AuthToken();
        User user = new User();
        LoginResult result = new LoginResult();
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();

        user.setUsername(r.getUsername());
        user.setPassword(r.getPassword());

        try {
            //Check for valid username and matching password
            if (userDAO.findUser(user.getUsername()) != null && Objects.equals(user.getPassword(), userDAO.findUser(user.getUsername()).getPassword())) {
                newAuthToken.setAndGenerateAuthToken();
                newAuthToken.setUsername(user.getUsername());
                result.setUsername(user.getUsername());
                result.setAuthToken(newAuthToken.getAuthToken());
                authDAO.insertAuthToken(newAuthToken);
            } else {
                result.setMessage("Error: unauthorized");
            }

            return result;
        } catch (DataAccessException e) {
            result.setMessage(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}