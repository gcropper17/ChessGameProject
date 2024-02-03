package Handlers;
import Requests.CreateGameRequest;
import Results.CreateGameResult;
import Services.CreateGameService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) {
        Gson gson = new Gson();
        CreateGameService service = new CreateGameService();
        HandlerHelpers helper = new HandlerHelpers();

        //Parse the request body into a CreateGameRequest object
        CreateGameRequest request = gson.fromJson(requestData.body(), CreateGameRequest.class);
        request.setAuthToken(requestData.headers("Authorization"));

        CreateGameResult result = service.createGame(request);

        //Set HTTP response status based on result of createGame
        response.status(helper.setResStatus(result.getMessage()));

        return gson.toJson(result);
    }
}