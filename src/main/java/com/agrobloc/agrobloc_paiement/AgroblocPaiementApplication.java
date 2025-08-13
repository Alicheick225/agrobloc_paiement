package com.agrobloc.agrobloc_paiement;

import com.paydunya.neptune.PaydunyaCheckoutStore;
import com.paydunya.neptune.PaydunyaSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgroblocPaiementApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgroblocPaiementApplication.class, args);

		PaydunyaSetup setup = new PaydunyaSetup();
		setup.setMasterKey("1L0RVAOh-BC0H-QXDt-Fstk-wu3wnKbq6Iq4");
		setup.setPrivateKey("test_private_iYgKFGAtrA56vHAozlLFtRXBevv");
		setup.setPublicKey("test_public_adWDveLhb8GtvsTGPo2Q6WTl2fs");
		setup.setToken("YnPkmb68lVpdla4It68G");
		setup.setMode("test"); // Optionnel. Utilisez cette option pour les paiements tests.


        //Configuration des informations de votre service///Configuration des informations de votre service/entreprise
		PaydunyaCheckoutStore store = new PaydunyaCheckoutStore();
		store.setName("Agrobloc"); // Seul le nom est requis
		store.setCallbackUrl("https://balanced-cheerful-mule.ngrok-free.app/webhook/paydunya");
	}
}
