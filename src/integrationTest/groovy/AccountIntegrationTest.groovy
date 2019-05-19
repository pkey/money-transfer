import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.*
import spock.lang.Specification

class AccountIntegrationTest extends Specification {

    OkHttpClient client
    String url = "http://localhost:4567"
    static final MediaType JSON = MediaType.get("application/json; charset=utf-8")

    def setup() {
        this.client = new OkHttpClient()
    }

    def "return 404 if account does not exist"() {
        given:
        Request request = new Request.Builder()
                .url(url + "/account/1")
                .build()

        when:
        Response response = this.client.newCall(request).execute()

        then:
        response.code() == 404
    }

    def "creates new account"() {
        given:
        Request request = new Request.Builder()
                .url(url + "/account")
                .post(RequestBody.create(JSON, ""))
                .build()
        when:
        Response response = this.client.newCall(request).execute()
        JsonObject json = parseJson(response.body().string())
        then:
        json.get("id") != null
        json.get("balance") != null
        response.code() == 200
    }

    def "returns an account"() {
        given:
        JsonObject account = parseJson(createAccount().body().string())
        Request request = new Request.Builder()
                .url(url + "/account/" + account.get("id").asString)
                .build()
        when:
        Response response = this.client.newCall(request).execute()
        JsonObject responseBody = parseJson(response.body().string())
        then:
        response.code() == 200
        responseBody.get("id").asString == account.get("id").asString
        responseBody.get("balance").asInt == 0
    }

    def "deletes an account"() {
        given:
        JsonObject account = parseJson(createAccount().body().string())
        Request request = new Request.Builder()
                .url(url + "/account/" + account.get("id").asString)
                .delete()
                .build()
        when:
        Response response = this.client.newCall(request).execute()
        then:
        response.code() == 200
        getAccount(account.get("id").asString).code() == 404
    }

    def "updates an account balance"() {
        given:
        JsonObject account = parseJson(createAccount().body().string())
        JsonObject requestBody = new JsonObject()
        requestBody.addProperty("id", account.get("id").asString)
        requestBody.addProperty("balance", 10)
        Request request = new Request.Builder()
                .url(url + "/account/" + account.get("id").asString)
                .put(RequestBody.create(JSON, requestBody.toString()))
                .build()
        when:
        Response response = this.client.newCall(request).execute()
        JsonObject responseBody = parseJson(response.body().string())
        JsonObject updatedAccount = parseJson(getAccount(account.get("id").asString).body().string())

        then:
        response.code() == 200
        responseBody.get("id").asString == account.get("id").asString
        responseBody.get("balance").asInt == 10
        updatedAccount.get("balance").asInt == 10
    }

    def "doesn't update account to a negative balance"() {
        given:
        JsonObject account = parseJson(createAccount().body().string())
        JsonObject requestBody = new JsonObject()
        requestBody.addProperty("id", account.get("id").asString)
        requestBody.addProperty("balance", -1)
        Request request = new Request.Builder()
                .url(url + "/account/" + account.get("id").asString)
                .put(RequestBody.create(JSON, requestBody.toString()))
                .build()
        when:
        Response response = this.client.newCall(request).execute()

        then:
        response.code() == 400
    }

    def "transfers money from one account to another"() {
        given:
        JsonObject accountFrom = parseJson(createAccount().body().string())
        JsonObject accountTo = parseJson(createAccount().body().string())
        updateAccount(accountFrom.get("id").asString, 10)
        JsonObject requestBody = new JsonObject()
        requestBody.addProperty("accountFromId", accountFrom.get("id").asString)
        requestBody.addProperty("accountToId", accountTo.get("id").asString)
        requestBody.addProperty("amount", 10)
        Request request = new Request.Builder()
                .url(url + "/transfer")
                .post(RequestBody.create(JSON, requestBody.toString()))
                .build()

        when:
        Response response = this.client.newCall(request).execute()
        JsonObject updatedAccountFrom = parseJson(getAccount(accountFrom.get("id").asString).body().string())
        JsonObject updatedAccountTo = parseJson(getAccount(accountTo.get("id").asString).body().string())
        then:
        response.code() == 200
        updatedAccountFrom.get("balance").asInt == 0
        updatedAccountTo.get("balance").asInt == 10
    }

    def "can't transfer to the same account"() {
        given:
        JsonObject accountFrom = parseJson(createAccount().body().string())
        updateAccount(accountFrom.get("id").asString, 10)
        JsonObject body = new JsonObject()
        body.addProperty("accountFromId", accountFrom.get("id").asString)
        body.addProperty("accountToId", accountFrom.get("id").asString)
        body.addProperty("amount", 10)
        Request request = new Request.Builder()
                .url(url + "/transfer")
                .post(RequestBody.create(JSON, body.toString()))
                .build()

        when:
        Response response = this.client.newCall(request).execute()
        JsonObject updatedAccountFrom = parseJson(getAccount(accountFrom.get("id").asString).body().string())
        then:
        response.code() == 400
        updatedAccountFrom.get("balance").asInt == 10
    }

    def "doesn't allow to transfer if insufficient funds"() {
        given:
        JsonObject accountFrom = parseJson(createAccount().body().string())
        JsonObject accountTo = parseJson(createAccount().body().string())
        JsonObject body = new JsonObject()
        body.addProperty("accountFromId", accountFrom.get("id").asString)
        body.addProperty("accountToId", accountTo.get("id").asString)
        body.addProperty("amount", 10)
        Request request = new Request.Builder()
                .url(url + "/transfer")
                .post(RequestBody.create(JSON, body.toString()))
                .build()
        when:
        Response response = this.client.newCall(request).execute()
        JsonObject json = parseJson(response.body().string())
        JsonObject updatedAccountFrom = parseJson(getAccount(accountFrom.get("id").asString).body().string())
        JsonObject updatedAccountTo = parseJson(getAccount(accountTo.get("id").asString).body().string())

        then:
        response.code() == 400
        updatedAccountFrom.get("balance").asInt == 0
        updatedAccountTo.get("balance").asInt == 0
    }

    def "doesn't allow to transfer non positive amount"() {
        given:
        JsonObject accountFrom = parseJson(createAccount().body().string())
        JsonObject body = new JsonObject()
        body.addProperty("accountFromId", accountFrom.get("id").asString)
        body.addProperty("accountToId", "some acc")
        body.addProperty("amount", amount)
        Request request = new Request.Builder()
                .url(url + "/transfer")
                .post(RequestBody.create(JSON, body.toString()))
                .build()
        when:
        Response response = this.client.newCall(request).execute()

        then:
        response.code() == 400

        where:
        amount << [-1, 0]
    }

    Response createAccount() {
        JsonObject body = new JsonObject()
        Request request = new Request.Builder()
                .url(url + "/account")
                .post(RequestBody.create(JSON, body.toString()))
                .build()
        return this.client.newCall(request).execute()
    }

    Response updateAccount(String id, int balance) {
        JsonObject body = new JsonObject()
        body.addProperty("id", id)
        body.addProperty("balance", balance)
        Request request = new Request.Builder()
                .url(url + "/account/" + id)
                .put(RequestBody.create(JSON, body.toString()))
                .build()
        return this.client.newCall(request).execute()
    }

    Response getAccount(String id) {
        Request request = new Request.Builder()
                .url(url + "/account/" + id)
                .build()
        return this.client.newCall(request).execute()
    }

    JsonObject parseJson(String json) {
        JsonElement jsonElement = new Gson().fromJson(json, JsonElement.class)
        return jsonElement.getAsJsonObject()
    }
}