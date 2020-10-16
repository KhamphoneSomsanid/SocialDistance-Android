package com.laodev.socialdis.util;

import android.net.Uri;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class PaypalUtil {

    public static final String CONFIG_CLIENT_SANDBOXID = "Ae3heUmxHwa30Ga2xHQwiTa5CzQIGh7D8ygS60HcaiFmilaq8wJ_EwThm3JAYBTChJYt8x3JvzXrdWzH";
    public static final String CONFIG_CLIENT_LIVEID = "AZPD60FnJrNOHZz3-t7y-FySmge4VJZHxJVDHFWp0lvo4bt5NkrZeDKDnQ7Oryt2tLn6oY2x42Vx7qwO";
    public static final String CONFIG_CLIENT_SECRET = "EOFMmwdWKWnbHIH6KchzExOkfFKSwdlwynVXRTy6U2i1azecGTlSX1JX5m7kVX0V7jSgw7poo6OUyF4y";

    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    public static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_LIVEID)
            .merchantName("My Product")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacey"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

}
