package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;

public class ApiCalls {

    public String getCall(String url) {
        HttpGet get = new HttpGet(url);
        HttpResponse response = execute(auth(get));
        return validateResponse(response, url);
    }

    public String postCall(String url, String payload) {
        HttpPost post = new HttpPost(url);
        post = (HttpPost) auth(post);
        post.setEntity(stringToStringEntity(payload));
        HttpResponse response = execute(auth(post));
        return validateResponse(response, url);
    }

    public String putCall(String url, String payload) {
        HttpPut put = new HttpPut(url);
        put = (HttpPut) auth(put);
        put.setEntity(stringToStringEntity(payload));
        HttpResponse response = execute(auth(put));
        return validateResponse(response, url);
    }

    public String deleteCall(String url) {
        HttpDelete delete = new HttpDelete(url);
        HttpResponse response = execute(auth(delete));
        return validateResponse(response, url);
    }

    private String validateResponse(HttpResponse response, String url) {
        int statusCode = response.getStatusLine().getStatusCode();
        String message = getHttpResponseBody(response);
        Boolean passStatusCode = Integer.toString(statusCode).contains("20");
        Assert.assertTrue(statusCode + message + " The url you sent in was " + url, passStatusCode);
        return message;
    }

    private HttpRequest auth(HttpRequest httpRequest) {
        UsernamePasswordCredentials creds =
                new UsernamePasswordCredentials(Config.getJiraUsername(), Config.getJiraPassword());
        Header header = null;
        try {
            header = new BasicScheme(StandardCharsets.UTF_8).authenticate(creds, httpRequest, null);
        } catch (AuthenticationException e) {
            //Not sure what to throw here
            throw new IllegalArgumentException("");
        }
        httpRequest.addHeader(header);
        httpRequest.setHeader("X-Atlassian-Token", "nocheck");
        return httpRequest;
    }

    private HttpResponse execute(HttpRequest httpRequest) {

        HttpClient http = HttpClientBuilder.create().build();
        HttpResponse response = null;
        try {
            response = http.execute((HttpUriRequest) httpRequest);
            return response;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String getHttpResponseBody(HttpResponse httpResponse) {
        HttpEntity entity = httpResponse.getEntity();
        try {
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return "";
        }
    }

    private StringEntity stringToStringEntity(String payload) {

        StringEntity playloadEntity = new StringEntity(payload, "UTF-8");
        playloadEntity.setContentType("application/json");
        return playloadEntity;
    }

    protected int getStatus(HttpResponse httpResponse) {
        int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        return httpStatusCode;
    }

    public String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
            br.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Undable to load file " + filename);
        }
        return result;
    }
}
