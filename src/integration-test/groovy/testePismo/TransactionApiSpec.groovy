package testePismo

import api.v1.Account
import grails.testing.mixin.integration.Integration
import grails.plugins.rest.client.RestBuilder
import grails.transaction.Rollback
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import api.v1.FixtureService
import spock.lang.Shared

@Integration
@Rollback
class TransactionApiSpec extends Specification {

    @Shared String token
    FixtureService fixtureService
    @Autowired
    private TestRestTemplate restTemplate
    private RestTemplate patchRestTemplate

    def setup() {
        token = login()
        fixtureService.createAccount()
        fixtureService.setupOperationType()
        fixtureService.createTransactions()
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    def cleanup() {
    }

    private String login() {
        def rest = new RestBuilder()
        def data = [username:'api.pismo', password:'pismoapi']
        def resp = rest.post("http://localhost:${serverPort}/testePismo/login") {
            accept('application/json')
            contentType('application/json')
            json(data)
        }
        println(resp.json.access_token)
        resp.json.access_token
    }

    void "POST /transactions"() {
        given:
        def rest = new RestBuilder()
        def data = [amount:100.0, account_id: Account.first().id, operation_type_id:1]
        when:
        def resp = rest.post("http://localhost:${serverPort}/testePismo/transactions") {
            accept('application/json')
            contentType('application/json')
            json(data)
            header'X-Auth-Token', token
        }
        then:
        resp.status == 201
    }

    void "POST /accounts"() {
        given:
        def rest = new RestBuilder()
        def data = ["available_credit_limit":["amount":1000.0], "available_withdrawal_limit":["amount":500.00]]
        when:
        def resp = rest.post("http://localhost:${serverPort}/testePismo/accounts") {
            accept('application/json')
            contentType('application/json')
            json(data)
            header'X-Auth-Token', token
        }
        then:
        resp.status == 201
    }

    void "POST /payments"() {
        given:
        def rest = new RestBuilder()
        def data = [[amount:150, account_id: Account.first().id], [amount:150, account_id: Account.first().id]]
        when:
        def resp = rest.post("http://localhost:${serverPort}/testePismo/payments") {
            accept('application/json')
            contentType('application/json')
            json(data)
            header'X-Auth-Token', token
        }
        then:
        resp.status == 201
    }

    void "GET /accounts/limits"() {
        given:
        def rest = new RestBuilder()

        when:
        def resp = rest.get("http://localhost:${serverPort}/testePismo/accounts/limits") {
            accept('application/json')
            contentType('application/json')
            header'X-Auth-Token', token
        }
        then:
        resp.status == 200
    }

    void "PATCH /accounts"() {
        given:
        def rest = new RestBuilder()
        def data = """{
            \t"available_credit_limit": {
            \t\t"amount": 10
            \t},
            \t"available_withdrawal_limit": {
            \t\t"amount": 20
            \t}
        }"""
        when:

        def resourceUrl = "http://localhost:${serverPort}/testePismo/accounts/${Account.first().id}"
        ResponseEntity resp =
                patchRestTemplate.exchange(resourceUrl, HttpMethod.PATCH, getPostRequestHeaders(data), Account.class)

        then:
        HttpStatus.OK == resp.getStatusCode()
    }

    public HttpEntity getPostRequestHeaders(String jsonPostBody) {
        List acceptTypes = new ArrayList();
        acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON);
        reqHeaders.setAccept(acceptTypes);
        ////reqHeaders.add('X-Auth_token',token);
        reqHeaders.set('X-Auth-Token',token)

        return new HttpEntity(jsonPostBody, reqHeaders)
    }

}
