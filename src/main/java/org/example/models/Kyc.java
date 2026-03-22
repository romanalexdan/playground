package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Kyc {

    @JsonProperty("user_id")
    public String userId;

    public KycStatus status;
}


