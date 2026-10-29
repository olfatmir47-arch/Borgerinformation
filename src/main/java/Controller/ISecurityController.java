package Controller;


import io.javalin.http.Context;

public interface ISecurityController {
    void login(Context ctx); // to get a token
    void authenticate(Context ctx); // to verify roles inside token
    void authorize(Context ctx);
}
