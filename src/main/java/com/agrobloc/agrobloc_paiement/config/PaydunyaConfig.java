package com.agrobloc.agrobloc_paiement.config;

import com.paydunya.neptune.PaydunyaSetup;
import com.paydunya.neptune.PaydunyaCheckoutStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaydunyaConfig {

    @Bean
    public PaydunyaSetup paydunyaSetup() {
        PaydunyaSetup setup = new PaydunyaSetup();
        setup.setMasterKey("1L0RVAOh-BC0H-QXDt-Fstk-wu3wnKbq6Iq4");
        setup.setPrivateKey("test_private_iYgKFGAtrA56vHAozlLFtRXBevv");
        setup.setPublicKey("test_public_adWDveLhb8GtvsTGPo2Q6WTl2fs");
        setup.setToken("YnPkmb68lVpdla4It68G");
        setup.setMode("test");
        return setup;
    }

    @Bean
    public PaydunyaCheckoutStore paydunyaStore() {
        PaydunyaCheckoutStore store = new PaydunyaCheckoutStore();
        store.setName("Agrobloc");
        store.setCallbackUrl("https://balanced-cheerful-mule.ngrok-free.app/webhook/paydunya");
        return store;
    }
}
