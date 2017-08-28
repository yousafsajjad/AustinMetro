package com.oyster.card.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by myousaf on 7/21/17.
 */
@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private String cardGUID;

    @Column
    private double balance;

    @Temporal(TemporalType.DATE)
    @Column
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column
    private Date updatedDate;

    @PrePersist
    private void onCreate() {
        createdDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @PreUpdate
    private void onUpdate() {
        updatedDate = new Date();
    }

    public String getCardGUID() {
        return cardGUID;
    }

    public void setCardGUID(String cardGUID) {
        this.cardGUID = cardGUID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
