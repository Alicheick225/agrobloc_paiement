package com.agrobloc.agrobloc_paiement.repository;

import com.agrobloc.agrobloc_paiement.model.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserWalletRepository extends JpaRepository<UserWallet, UUID> {
}
