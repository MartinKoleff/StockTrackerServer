package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    private Long id;

    @Column(
            name = "stock_id",
            nullable = false
    )
    private Long stockId;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "acronym",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String acronym;

    @Column(
            name = "exchange",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String exchange;

    @Column(
            name = "country",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String country;

    @Column(
            name = "country_code",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @SerializedName("country_code")
    private String countryCode;

    @Column(
            name = "city",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String city;

    @Column(
            name = "website",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String website;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "stock_id",
            nullable = false,
            insertable=false,
            updatable=false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "stock_exchange_fk"
            )
    )
    private Stock stock;

    @Override
    public String toString() {
        return "StockExchange{" +
                "id=" + id +
                ", stockId=" + stockId +
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
