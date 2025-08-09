package com.cosmetic.analysis.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductInfoResponse {
    private String notifNo;
    private String product;
    private String company;
    private boolean status;
    private LocalDate dateNotif;
    private List<String> substanceDetected;
}
