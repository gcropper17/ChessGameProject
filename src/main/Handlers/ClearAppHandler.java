package Handlers;
import Results.ClearAppResult;
import Services.ClearAppService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearAppHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) throws DataAccessException {
        Gson gson = new Gson();
        ClearAppService service = new ClearAppService();
        HandlerHelpers helper = new HandlerHelpers();

        ClearAppResult result = service.clear();

        //Set HTTP response status based on clear app result
        response.status(helper.setResStatus(result.getMessage()));

        return gson.toJson(result);
    }
}