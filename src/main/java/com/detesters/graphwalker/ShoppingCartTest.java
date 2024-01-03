package com.detesters.graphwalker;

import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.tinylog.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.assertj.core.api.Assertions.assertThat;

@GraphWalker(value = "quick_random(time_duration(1))")
public class ShoppingCartTest extends ExecutionContext implements com.detesters.graphwalker.ShoppingCart {

    @Override
    public void v_ShoppingCart() {
        Logger.trace("v_ShoppingCart in ShoppingCart");
    }

    @Override
    public void e_ProceedToCheckout() {
        Logger.trace("e_ProceedToCheckout");
        $("div.checkout a").click();
    }

    @Override
    public void v_PersonalInformation() {
        Logger.trace("v_PersonalInformation");
        assertThat($$("h1").texts()).as("Titles").contains("1 PERSONAL INFORMATION");
    }

    @Override
    public void e_OrderAsGuest() {
        Logger.trace("e_OrderAsGuest");
        String orderAsGuestTab = "a[aria-controls='checkout-guest-form']";

        if (StringUtils.defaultString($(orderAsGuestTab).getAttribute("aria-selected")).equals("true")) {
            Logger.trace("Tab already selected");
        } else {
            $(orderAsGuestTab).click();
        }
    }

    @Override
    public void v_Addresses() {
        Logger.trace("v_Addresses");
        $("input#field-id_gender-1").click();
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName.toLowerCase() + "@detesters.test";
        Logger.trace("firstName=" + firstName + ", lastName=" + lastName + ", email=" + email);
        $("input#field-firstname").sendKeys(firstName);
        $("input#field-lastname").sendKeys(lastName);
        $("input#field-email").sendKeys(email);
        $("input[name='psgdpr']").click();
        $("input[name='customer_privacy']").click();
        $("div#checkout-guest-form button.continue").click();
    }

    @Override
    public void e_GoToShop() {
        Logger.trace("e_GotoShop");
        $("img.logo").click();
    }

    @Override
    public void v_Search() {
        Logger.trace("v_Search");
    }
}
