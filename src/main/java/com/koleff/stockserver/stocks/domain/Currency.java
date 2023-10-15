package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "Currency")
@Table(name = "currency")
@NoArgsConstructor
@AllArgsConstructor
public@Data class Currency implements Serializable {
    @Id
    @SequenceGenerator(
            name = "currency_sequence",
            sequenceName = "currency_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "currency_sequence"
    )
    @Column(
            name = "id"
    )
    @SerializedName("id")
    private Long id;

    @Column(
            name = "code",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Code must not be empty.")
    @SerializedName("code")
    private String code;

    @Column(
            name = "symbol",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Symbol must not be empty.")
    @SerializedName("symbol")
    private String symbol;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    @NotNull(message = "Name must not be empty.")
    @SerializedName("name")
    private String name;

    @OneToOne //Doesn't need to be bidirectional
    private StockExchange stockExchange;

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}