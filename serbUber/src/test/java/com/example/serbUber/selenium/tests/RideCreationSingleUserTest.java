package com.example.serbUber.selenium.tests;

import com.example.serbUber.selenium.helper.LoginHelper;
import com.example.serbUber.selenium.pages.HomePage;
import com.example.serbUber.selenium.tests.bases.RideCreationSingleUserTestBase;
import org.junit.jupiter.api.*;

import static com.example.serbUber.selenium.helper.Constants.EXISTING_PASSWORD;
import static com.example.serbUber.selenium.helper.Constants.USER_WITH_NO_TOKENS_EMAIL;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RideCreationSingleUserTest extends RideCreationSingleUserTestBase {

    @Test
    @DisplayName("T1-Ride creation failed, start and end point are not selected")
    public void rideCreationFailedStartAndEndPointNotSelectedTest() {
        HomePage homePage = LoginHelper.login(chromeDriver, USER_WITH_NO_TOKENS_EMAIL, EXISTING_PASSWORD);




    }

}
