package com.koleff.stockserver.stocks.domain;

import jakarta.persistence.*;

@Entity(name = "StockExchange")
@Table(name = "stock_exchange")
public class StockExchange {
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

    @ManyToOne
//    @MapsId("id")
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

    public StockExchange(Long id, Long stockId, String name, String acronym, String exchange, String country, String countryCode, String city, String website) {
        this.id = id;
        this.stockId = stockId;
        this.name = name;
        this.acronym = acronym;
        this.exchange = exchange;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
        this.website = website;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

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
