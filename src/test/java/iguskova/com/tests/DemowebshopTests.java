package iguskova.com.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class DemowebshopTests {

    @Test
    @DisplayName("Добавление товара в wishlist")
    void addProductToWishList() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("addtocart_14.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/14/2")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is("(1)"));
    }


    @Test
    @DisplayName("Проверка наличия товара в wishlist с куки")
    void checkProductInWishList() {
        step("Добавить товар в вишлист и подложить куки", () -> {
            String cartCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("addtocart_14.EnteredQuantity=1")
                            .when()
                            .post("http://demowebshop.tricentis.com/addproducttocart/details/14/2")
                            .then()
                            .statusCode(200)
                            .extract()
                            .cookie("Nop.customer");
            step("Открыть минимальный элемент", () ->
                    open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png"));

            step("Подложить куки", () ->
                    getWebDriver().manage().addCookie(new Cookie("Nop.customer", cartCookie)));

            step("Открыть вишлист", () ->
                    open("http://demowebshop.tricentis.com/wishlist"));

            step("Проверить, что в вишлисте 1 товар", () -> {
                $$(".wishlist-qty").shouldHave(size(1));
                $(".qty-input").shouldHave(value("1"));
            });
        });
    }
}
