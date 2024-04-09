package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webserver.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestParserTest {

    @Test
    void http_헤더의_첫_줄에서_method_path_protocol를_추출한다() {
        //given
        List<String> headerString = new ArrayList<>();
        headerString.add("GET /index.html HTTP/1.1");
        headerString.add("Host: localhost:8080");
        headerString.add("Connection: keep-alive");
        headerString.add("Accept: */*");

        //when
        HttpRequest request = HttpRequestParser.parse(headerString);

        //then
        Assertions.assertThat(request.getMethod()).isEqualTo("GET");
        Assertions.assertThat(request.getRequestUri()).isEqualTo("/index.html");
        Assertions.assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
    }

    @Test
    void path에서_query_parameter를_추출한다(){
        //given
        List<String> headerString = new ArrayList<>();
        headerString.add("GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1");
        headerString.add("Host: localhost:8080");
        headerString.add("Connection: keep-alive");
        headerString.add("Accept: */*");

        //when
        HttpRequest request = HttpRequestParser.parse(headerString);

        //then
        Map<String, String> queryParams = request.getQueryParams();
        Assertions.assertThat(queryParams.get("userId")).isEqualTo("cu");
        Assertions.assertThat(queryParams.get("password")).isEqualTo("password");
        Assertions.assertThat(queryParams.get("name")).isEqualTo("이동규");
        Assertions.assertThat(queryParams.get("email")).isEqualTo("brainbackdoor@gmail.com");

    }

    @Test
    void path에서_빈_query_parameter를_추출한다(){
        //given
        List<String> headerString = new ArrayList<>();
        headerString.add("GET /user/create?userId=&password=&name=&email= HTTP/1.1");
        headerString.add("Host: localhost:8080");
        headerString.add("Connection: keep-alive");
        headerString.add("Accept: */*");

        //when
        HttpRequest request = HttpRequestParser.parse(headerString);

        //then
        Map<String, String> queryParams = request.getQueryParams();
        Assertions.assertThat(queryParams.get("userId")).isEqualTo("");
        Assertions.assertThat(queryParams.get("password")).isEqualTo("");
        Assertions.assertThat(queryParams.get("name")).isEqualTo("");
        Assertions.assertThat(queryParams.get("email")).isEqualTo("");
    }
}
