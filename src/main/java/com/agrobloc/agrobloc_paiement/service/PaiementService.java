package com.agrobloc.agrobloc_paiement.service;

import com.paydunya.neptune.PaydunyaSetup;
import com.paydunya.neptune.PaydunyaCheckoutStore;
import com.paydunya.neptune.PaydunyaCheckoutInvoice;
import org.springframework.stereotype.Service;

@Service
public class PaiementService {

    private final PaydunyaSetup setup;
    private final PaydunyaCheckoutStore store;

    public PaiementService(PaydunyaSetup setup, PaydunyaCheckoutStore store) {
        this.setup = setup;
        this.store = store;
    }

    public PaydunyaCheckoutInvoice createInvoice() {
        return new PaydunyaCheckoutInvoice(setup, store);
    }
}
