package com.koleff.stockserver.stocks.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Stock")
@Table(name = "stock")
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @SequenceGenerator(
            name = "stock_sequence",
            sequenceName = "stock_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "stock_sequence"
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "tag",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String tag;

    @Column(
            name = "country",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String country;

    @Column(
            name = "has_intra_day",
            nullable = false
    )
    private Boolean hasIntraDay;

    @Column(
            name = "has_end_of_day",
            nullable = false
    )
    private Boolean hasEndOfDay;

    @OneToMany(
            mappedBy = "stock",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<EndOfDay> endOfDay = new ArrayList<>();

    @OneToMany(
            mappedBy = "stock",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<IntraDay> intraDay = new ArrayList<>();

    @OneToMany(
            mappedBy = "stock",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<StockExchange> stockExchange = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getHasIntraDay() {
        return hasIntraDay;
    }

    public void setHasIntraDay(Boolean hasIntraDay) {
        this.hasIntraDay = hasIntraDay;
    }

    public Boolean getHasEndOfDay() {
        return hasEndOfDay;
    }

    public void setHasEndOfDay(Boolean hasEndOfDay) {
        this.hasEndOfDay = hasEndOfDay;
    }

    public List<EndOfDay> getEndOfDay() {
        return endOfDay;
    }

    public List<IntraDay> getIntraDay() {
        return intraDay;
    }

    public List<StockExchange> getStockExchange() {
        return stockExchange;
    }

    public void setEndOfDay(List<EndOfDay> endOfDay) {
        this.endOfDay = endOfDay;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", country='" + country + '\'' +
                ", hasIntraDay=" + hasIntraDay +
                ", hasEndOfDay=" + hasEndOfDay +
                '}';
    }
}
