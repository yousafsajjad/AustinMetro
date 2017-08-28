package com.oyster.card.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oyster.card.data.CardTransaction;

/**
 * Created by myousaf on 7/21/17.
 */
@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    @Query("Select ct from CardTransaction ct where ct.cardGUID = ?1")
    CardTransaction findByCardGUID(@Param("cardGUID") String cardGUID);

    @Query("Select ct from CardTransaction ct where ct.cardGUID = ?1 AND ct.status = ?2")
    CardTransaction findByCardGUIDAndInProgressStatus(@Param("cardGUID") String cardGUID, @Param("status") String status);
}
