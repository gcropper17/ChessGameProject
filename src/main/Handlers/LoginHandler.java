package Handlers;
import Requests.LoginRequest;
import Results.LoginResult;
import Services.LoginService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) {
        Gson gson = new Gson();
        LoginService service = new LoginService();
        HandlerHelpers helper = new HandlerHelpers();

        //Parse the request body into a LoginRequest object
        LoginRequest request = gson.fromJson(requestData.body(), LoginRequest.class);

        LoginResult result = service.login(request);

        //Set HTTP response status based on result of login
        response.status(helper.setResStatus(result.getMessage()));

        return gson.toJson(result);
    }
}