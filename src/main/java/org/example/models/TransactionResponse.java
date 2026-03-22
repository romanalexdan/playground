package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionResponse {

    public List<Transaction> data;

    @JsonProperty("next_cursor")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public String nextCursor;
}
