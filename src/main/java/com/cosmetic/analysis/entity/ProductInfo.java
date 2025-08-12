package com.cosmetic.analysis.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_info")
@Data
public class ProductInfo {

    @Id
    @Column(name = "notif_no", length = 50)
    private String notifNo;

    private String product;
    private String company;
    private String manufacturer;

    @Column(name = "date_notif")
    private LocalDate dateNotif;  

    private boolean status;  
}
