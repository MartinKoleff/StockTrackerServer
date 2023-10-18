package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "Timezone")
@Table(name = "timezone")
@NoArgsConstructor
@AllArgsConstructor
public@Data class Timezone implements Serializable {
    @Id
    @SequenceGenerator(
            name = "timezone_sequence",
            sequenceName = "timezone_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "timezone_sequence"
    )
    @Column(
            name = "id"
    )
    @SerializedName("id")
    private Long id;

    @Column(
            name = "timezone",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Timezone must not be empty.")
    @SerializedName("timezone")
    private String timezone;

    @Column(
            name = "abbreviation",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Abbreviation must not be empty.")
    @SerializedName("abbreviation")
    private String abbreviation; //Timezone abbreviation

    @Column(
            name = "abbreviation_dst",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Abbreviation daylight-saving time must not be empty.")
    @SerializedName("abbreviation_dst")
    private String abbreviationDst; //Summer time timezone abbreviation (DST -> daylight-saving time)

    @OneToOne //Doesn't need to be bidirectional
    private StockExchange stockExchange;

    @Override
    public String toString() {
        return "Timezone{" +
                "id=" + id +
                ", timezone='" + timezone + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", abbreviation_dst='" + abbreviationDst + '\'' +
                '}';
    }
}
