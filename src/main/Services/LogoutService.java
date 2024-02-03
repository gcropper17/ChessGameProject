package Services;
import DataAccess.AuthDAO;
import Models.AuthToken;
import Requests.LogoutRequest;
import Results.LogoutResult;
import dataAccess.DataAccessException;
/**
 * Class that determines if logging out is possible.
 */
public class LogoutService {
    /**
     * Head method that determines of logging out is valid.
     * @param r Request to log out.
     * @return Return successful or unsuccessful result.
     */
    public LogoutResult logout(LogoutRequest r) {
        AuthToken authToken = new AuthToken();
        AuthDAO authDAO = new AuthDAO();
        LogoutResult result = new LogoutResult();

        authToken.setAuthToken(r.getAuthToken());
        try {
            //AuthDAO.findAuthToken(authToken.getAuthToken());
            authDAO.removeAuthToken(authToken);
            return result;
        } catch (DataAccessException e) {
            result.setMessage(e.getMessage());
            return result;
        }
    }
}