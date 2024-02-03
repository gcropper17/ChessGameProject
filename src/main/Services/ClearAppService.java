package Services;
import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import Results.ClearAppResult;
import dataAccess.DataAccessException;

/**
 * Class that determines if clearing application is valid.
 */
public class ClearAppService {
    /**
     * Head function that will determine if clearing is valid.
     * @return returns successful or unsuccessful result.
     */
    public ClearAppResult clear() throws DataAccessException {
        AuthDAO.clear();
        GameDAO.clearAll();
        UserDAO.clear();

        return new ClearAppResult();
    }
}