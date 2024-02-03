package Handlers;
import Requests.ListGamesRequest;
import Results.ListGamesResult;
import Services.ListGamesService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) {
        Gson gson = new Gson();
        ListGamesService service = new ListGamesService();
        HandlerHelpers helper = new HandlerHelpers();

        //Parse the request body into a ListGamesRequest object
        ListGamesRequest request = new ListGamesRequest(requestData.headers("Authorization"));

        ListGamesResult result = service.listGames(request);

        //Set HTTP response status based on result of listGames
        response.status(helper.setResStatus(result.getMessage()));

        return gson.toJson(result);
    }
}