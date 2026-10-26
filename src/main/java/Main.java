
import Endpoints.AppConfig;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;


import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException {

        Javalin app = Javalin.create(config ->{config.jsonMapper(new JavalinJackson());}).start(7070);
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
        });

        AppConfig.configure(app);

    }
}
