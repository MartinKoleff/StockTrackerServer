package com.koleff.stockserver.stocks.domain;

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
