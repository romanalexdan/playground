package org.example.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transfer {

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    public TransferStatus status = TransferStatus.NO_STATUS;

    public String message;
    public String error;
}
