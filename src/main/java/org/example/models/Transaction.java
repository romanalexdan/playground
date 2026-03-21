package org.example.models;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public String id;

    public BigDecimal amount;
}
