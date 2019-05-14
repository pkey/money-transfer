import static spark.Spark.*;
import java.math.BigDecimal; 

public class AccountService {
    public static Account getAccount() { 
        get("/", (req, res) -> "Hello World");
    }
}