package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "IntraDay")
@Table(name = "intra_day")
@NoArgsConstructor
@AllArgsConstructor
public @Data class IntraDay implements Serializable {
    @Id
    @SequenceGenerator(
            name = "intra_day_sequence",
            sequenceName = "intra_day_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "intra_day_sequence"
    )
    @Column(
            name = "id"
    )
    @SerializedName("id")
    private Long id;

    @Column(
            name = "stock_id",
            nullable = false
    )
    @NotNull(message = "Stock id must not be empty.")
    @SerializedName("stock_id")
    private Long stockId;

    @Column(
            name = "open",
            nullable = false
    )
    @NotNull(message = "Open must not be empty.")
    @SerializedName("open")
    private Double open;

    @Column(
            name = "close",
            nullable = true
    )
    @SerializedName("close")
    private Double close;

    @Column(
            name = "high",
            nullable = false
    )
    @NotNull(message = "High must not be empty.")
    @SerializedName("high")
    private Double high;

    @Column(
            name = "low",
            nullable = false
    )
    @NotNull(message = "Low must not be empty.")
    @SerializedName("low")
    private Double low;

    @Column(
            name = "last",
            nullable = true
    )
    @SerializedName("last")
    private Double last;

    @Column(
            name = "volume",
            nullable = true
    )
    @SerializedName("volume")
    private Double volume;

    @Column(
            name = "date",
            nullable = false
    )
    @NotNull(message = "Date must not be empty.")
    @SerializedName("date")
    private String date;

    @ManyToOne //Doesn't need to be bidirectional
    @JoinColumn(
            name = "stock_id",
            nullable = false,
            insertable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "intra_day_fk"
            )
    )
    private Stock stock;

    @Override
    public String toString() {
        return "IntraDay{" +
                "id=" + id +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", last=" + last +
                ", volume=" + volume +
                ", date=" + date +
                '}';
    }
}