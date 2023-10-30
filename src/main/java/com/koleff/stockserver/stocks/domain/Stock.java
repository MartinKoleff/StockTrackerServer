package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
            name = "stock_exchange_id",
            nullable = false
    )
    @NotNull(message = "Stock exchange id must not be empty.")
    @SerializedName("stock_exchange_id")
    private Long stockExchangeId;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Name must not be empty.")
    @SerializedName("name")
    private String name;

    @Column(
            name = "tag",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Tag must not be empty.")
    @SerializedName("tag")
    private String tag;

    @Column(
            name = "has_intraday",
            nullable = false,
            columnDefinition = "boolean default false"
    )
    @NotNull(message = "Has intraday must not be empty.")
    @SerializedName("has_intraday")
    private Boolean hasIntraDay;

    @Column(
            name = "has_end_of_day",
            nullable = false,
            columnDefinition = "boolean default false"
    )
    @NotNull(message = "Has end of day must not be empty.")
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

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "stock_exchange_id",
            nullable = false,
            insertable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "stock_exchange_fk"
            )
    )
    private StockExchange stockExchange;

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", stock_exchange_id=" + stockExchangeId +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", has_intra_day=" + hasIntraDay +
                ", has_end_of_day=" + hasEndOfDay +
                '}';
    }
}
