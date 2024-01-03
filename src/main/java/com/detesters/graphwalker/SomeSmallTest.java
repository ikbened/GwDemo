package com.detesters.graphwalker;

import com.codeborne.selenide.WebDriverRunner;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.tinylog.Logger;

import java.util.List;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selenide.*;
import static org.assertj.core.api.Assertions.assertThat;

//@GraphWalker(value = "random(edge_coverage(100))", start = "v_InitialState")
@GraphWalker(value = "quick_random(edge_coverage(100))", start = "v_InitialState")
//@GraphWalker(value = "random(time_duration(60))", start = "v_InitialState")
//@GraphWalker(value = "random(length(5))", start = "v_InitialState")
//@GraphWalker(value = "random(edge_coverage(100) or time_duration(60))", start = "v_InitialState")
public class SomeSmallTest extends ExecutionContext implements com.detesters.graphwalker.SmallTest {

    @Override
    public void v_InitialState() {
        Logger.trace("v_InitialState");
        browser = "chrome";
    }

    @Override
    public void e_GoToShop() {
        Logger.trace("e_GoToShop");
        open("http://localhost:8080");
        WebDriverRunner.getWebDriver().manage().window().maximize();
        $("img.logo").shouldBe(visible, enabled);
        Logger.trace("title=" + title());
        assertThat(title()).as("title").isEqualTo("PrestaShop");
    }

    @Override
    public void e_IncQuantity() {
        Logger.trace("e_IncQuantity");
        $("i.touchspin-up").click();
        Logger.trace("New quantity=" + $("input#quantity_wanted").getAttribute("value"));
    }

    @Override
    public void v_Search() {
        Logger.trace("v_Search");
        $(By.name("s")).shouldBe(visible, enabled);
    }

    @Override
    public void e_SearchUnknownProduct() {
        String product = Faker.instance().regexify("[A-Z]{25}");
        Logger.trace("e_SearchUnknownProduct, unknown product=" + product);
        $(By.name("s")).sendKeys(Keys.chord(Keys.COMMAND, "A") + Keys.DELETE);
        $(By.name("s")).sendKeys(product + Keys.ENTER);
    }

    @Override
    public void v_NoMatchesFound() {
        Logger.trace("v_NoMatchesFound");
        $("#main h1").shouldHave(exactText("Search results"));
        $("#main h4").shouldHave(exactText("No matches were found for your search"));
    }

    @Override
    public void e_SearchKnownProduct() {
        Logger.trace("e_SearchKnownProduct");
        List<String> products = List.of("adventure", "Shirt");
        String product = Faker.instance().options().nextElement(products);
        Logger.trace("e_SearchKnownProduct, product=" + product);
        $(By.name("s")).sendKeys(Keys.chord(Keys.COMMAND, "A") + Keys.DELETE);
        $(By.name("s")).sendKeys(product + Keys.ENTER);
    }

    @Override
    public void v_SearchResult() {
        Logger.trace("v_SearchResult");
        SoftAssertions assertions = new SoftAssertions();

        String totalProducts = $("div.total-products").getText();

        assertThat(totalProducts).matches(Pattern.compile("(There is 1 product\\.)|(There are \\d+ products\\.)"));
        totalProducts = StringUtils.substringBefore(totalProducts, "product").trim();
        totalProducts = StringUtils.substringAfterLast(totalProducts, " ");
        assertions.assertThat(StringUtils.isNumeric(totalProducts)).isTrue();
        assertions.assertThat(Integer.parseInt(totalProducts)).isPositive();

        assertions.assertAll();
    }

    @Override
    public void e_ViewProduct() {
        Logger.trace("e_ViewProduct");
        $(By.xpath("//article[1]//a")).click();
    }

    @Override
    public void v_Product() {
        Logger.trace("v_Product");
        $("span.current-price-value").shouldBe(visible);
        $("button.add-to-cart").shouldBe(enabled);
        $("input#quantity_wanted").shouldBe(editable);
    }

    @Override
    public void e_ViewDescription() {
        Logger.trace("e_ViewDescription");

        String tab = "a[href='#description']";
        if (StringUtils.containsIgnoreCase($(tab).getAttribute("class"),
                "active")) {
            Logger.trace("Tab already active.");
        } else {
            $(tab).click();
        }
    }

    @Override
    public void v_ProductDescription() {
        Logger.trace("v_ProductDescription");
        String description = "div#description";
        $(description).shouldHave(cssClass("active"));
        Logger.trace("description=" + $(description).getText());
    }

    @Override
    public void e_ViewDetails() {
        Logger.trace("e_ViewDetails");

        String tab = "a[href='#product-details']";
        if (StringUtils.containsIgnoreCase($(tab).getAttribute("class"),
                "active")) {
            Logger.trace("Tab already active.");
        } else {
            $(tab).click();
        }
    }

    @Override
    public void v_ProductDetails() {
        Logger.trace("v_ProductDetails");
        String productDetails = "div#product-details";
        $(productDetails).shouldHave(cssClass("active"));
        Logger.trace("product-details=" + $(productDetails).getText());
    }

    @Override
    public void e_AddToCart() {
        Logger.trace("e_AddToCart");
        $("button.add-to-cart").click();
    }

    @Override
    public void v_ShoppingCart() {
        Logger.trace("v_ShoppingCart");
        $("h4.modal-title").shouldHave(text("Product successfully added to your shopping cart"));
        int quantity = Integer.parseInt($("span.product-quantity strong").getText());
        assertThat(quantity).as("quantity").isPositive();
    }

    @Override
    public void e_ContinueShopping() {
        Logger.trace("e_ContinueShopping");
        $(By.xpath("//button[text()='Continue shopping']")).click();
    }

}
