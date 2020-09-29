package com.cloud.book;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.config;

public class BookControllerTests {
    private final String ROOT_URI = "http://localhost:8080";
    private FormAuthConfig formConfig = new FormAuthConfig("/login", "username", "password");

    @Before("")
    public void setup() {
        config = config().redirect(
                RedirectConfig.redirectConfig().followRedirects(false));
    }

    @Test
    public void whenGetAllBooks_thenSuccess() {
        Response response = RestAssured.get(ROOT_URI + "/book-service/books");
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void whenAccessProtectedResourceWithoutLogin_thenRedirectToLogin() {
        Response response = RestAssured.post(ROOT_URI + "/book-service/books/1");
        Assert.assertEquals(HttpStatus.FOUND.value(), response.getStatusCode());
        Assert.assertEquals("http://localhost:8080/login",
                response.getHeader("Location"));
    }
     @Test
 public void whenAddNewBook_thenSuccess() {
             Book book = new Book("Baeldung", "How to spring cloud");
             Response bookResponse = RestAssured.given().auth()
               .form("admin", "admin", formConfig).and()
              .contentType(ContentType.JSON)
              .body(book)
              .post(ROOT_URI + "/book-service/books");
            Book result = bookResponse.as(Book.class);

             Assert.assertEquals(HttpStatus.OK.value(), bookResponse.getStatusCode());
            Assert.assertEquals(book.getAuthor(), result.getAuthor());
             Assert.assertEquals(book.getTitle(), result.getTitle());
         }

}
