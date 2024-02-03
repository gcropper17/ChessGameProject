package Handlers;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import Services.JoinGameService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) throws Exception {
        Gson gson = new Gson();
        JoinGameService service = new JoinGameService();
        HandlerHelpers helper = new HandlerHelpers();

        //Parse the request body into a JoinGameRequest object
        JoinGameRequest request = gson.fromJson(requestData.body(), JoinGameRequest.class);
        request.setAuthToken(requestData.headers("Authorization"));

        JoinGameResult result = service.joinGame(request);

        //Set HTTP response status based on result of joinGame
        response.status(helper.setResStatus(result.getMessage()));
        return gson.toJson(result);
    }
}