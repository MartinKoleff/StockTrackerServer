package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity(name = "StockExchange")
@Table(name = "stock_exchange")
@NoArgsConstructor
@AllArgsConstructor
public @Data class StockExchange implements Serializable {
    @Id
    @SequenceGenerator(
            name = "stock_exchange_sequence",
            sequenceName = "stock_exchange_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "stock_exchange_sequence"
    )
    @Column(
            name = "id"
    )
    @SerializedName("id")
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Name must not be empty.")
    @SerializedName("name")
    private String name;

    @Column(
            name = "acronym",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Acronym must not be empty.")
    @SerializedName("acronym")
    private String acronym;

    @Column(
            name = "exchange",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Exchange must not be empty.")
    @SerializedName("exchange")
    private String exchange;

    @Column(
            name = "country",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Country must not be empty.")
    @SerializedName("country")
    private String country;

    @Column(
            name = "country_code",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Country code must not be empty.")
    @SerializedName("country_code")
    private String countryCode;

    @Column(
            name = "city",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "City must not be empty.")
    @SerializedName("city")
    private String city;

    @Column(
            name = "website",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Website must not be empty.")
    @SerializedName("website")
    private String website;

    @OneToMany //Doesn't need to be bidirectional
    private List<Stock> stock;
            mappedBy = "stockExchange",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            orphanRemoval = false
    )

    @Override
    public String toString() {
        return "StockExchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", exchange='" + exchange + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
