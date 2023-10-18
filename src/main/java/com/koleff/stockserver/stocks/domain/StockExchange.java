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
public @Data class StockExchange implements Serializable{
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

    @Column(
            name = "timezone_id",
            nullable = false
    )
    @NotNull(message = "Timezone id must not be empty.")
    @SerializedName("timezone_id")
    private Long timezoneId;

    @Column(
            name = "currency_id",
            nullable = false
    )
    @NotNull(message = "Currency id must not be empty.")
    @SerializedName("currency_id")
    private Long currencyId;

    @OneToMany //Doesn't need to be bidirectional
    private List<Stock> stock;

    @OneToOne(
            mappedBy = "stockExchange",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            orphanRemoval = false
    )
    @JoinColumn(
            name = "timezone_id",
            nullable = false,
            insertable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "timezone_id_fk" //to check for foreign key...
            )
    )
    private Timezone timezone;

    @OneToOne(
            mappedBy = "stockExchange",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            orphanRemoval = false
    )
    @JoinColumn(
            name = "currency_id",
            nullable = false,
            insertable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "currency_id_fk" //to check for foreign key...
            )
    )
    private Currency currency;

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
                ", timezone=" + timezone +
                ", currency=" + currency +
                '}';
    }
}
