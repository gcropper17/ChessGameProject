package Handlers;
import Requests.LogoutRequest;
import Results.LogoutResult;
import Services.LogoutService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) {
        Gson gson = new Gson();
        LogoutService service = new LogoutService();
        HandlerHelpers helper = new HandlerHelpers();

        //Parse the request body into a LogoutRequest object
        LogoutRequest request = new LogoutRequest(requestData.headers("Authorization"));

        LogoutResult result = service.logout(request);

        //Set HTTP response status based on result of login
        response.status(helper.setResStatus(result.getMessage()));

        return gson.toJson(result);
    }
}