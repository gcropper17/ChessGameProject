package Handlers;
import Requests.RegisterRequest;
import Results.RegisterResult;
import Services.RegisterService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import spark.Route;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request requestData, Response response) {
        Gson gson = new Gson();
        RegisterService service = new RegisterService();
        HandlerHelpers helper = new HandlerHelpers();

        //Parse the request body into a RegisterRequest object
        RegisterRequest request = gson.fromJson(requestData.body(), RegisterRequest.class);

        RegisterResult result = service.register(request);

        //Set HTTP response status based on result of register
        response.status(helper.setResStatus(result.getMessage()));

        return gson.toJson(result);
    }
}