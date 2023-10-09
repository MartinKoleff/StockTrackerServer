package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity(name = "Stock")
@Table(name = "stock")
@NoArgsConstructor
@AllArgsConstructor
public @Data class Stock implements Serializable {
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
    @SerializedName("id")
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @SerializedName("name")
    private String name;

    @Column(
            name = "tag",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @SerializedName("tag")
    private String tag;

    @Column(
            name = "has_intraday",
            nullable = false
    )
    @SerializedName("has_intraday")
    private Boolean hasIntraDay;

    @Column(
            name = "has_end_of_day",
            nullable = false
    )
    @SerializedName("has_end_of_day")
    private Boolean hasEndOfDay;

    @OneToMany(
            mappedBy = "stock",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<EndOfDay> endOfDay;

    @OneToMany(
            mappedBy = "stock",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<IntraDay> intraDay;

    @OneToOne(
            mappedBy = "stock",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private StockExchange stockExchange;

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", hasIntraDay=" + hasIntraDay +
                ", hasEndOfDay=" + hasEndOfDay +
                '}';
    }
}
