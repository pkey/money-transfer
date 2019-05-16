/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.revolut.task;
import com.revolut.task.AccountService;
import static spark.Spark.*;
import java.util.UUID;
import com.google.gson.Gson;


public class App {
    public static void main(String[] args) {
       Gson gson = new Gson();

       get("/:id", (req, res) -> AccountService.getAccount(req.attribute(":id")));
       post("/", (req, res) -> AccountService.createAccount(), gson::toJson);
    }
}
