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

    @OneToMany(
            mappedBy = "stockExchange",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER,
            orphanRemoval = false
    )
    private List<Stock> stocks;  //Doesn't need to be bidirectional

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "timezone_id",
            nullable = false,
            insertable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "timezone_id_fk"
            )
    )
    private Timezone timezone;  //Doesn't need to be bidirectional

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "currency_id",
            nullable = false,
            insertable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "currency_id_fk"
            )
    )
    private Currency currency;  //Doesn't need to be bidirectional

    public StockExchange(Long id,
                         String name,
                         String acronym,
                         String country,
                         String countryCode,
                         String city,
                         String website,
                         Long timezoneId,
                         Long currencyId) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
        this.website = website;
        this.timezoneId = timezoneId;
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "StockExchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", website='" + website + '\'' +
                ", timezoneId=" + timezoneId +
                ", currencyId=" + currencyId +
                '}';
    }
}
