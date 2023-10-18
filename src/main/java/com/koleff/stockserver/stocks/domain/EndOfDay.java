package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "EndOfDay")
@Table(name = "end_of_day")
@NoArgsConstructor
@AllArgsConstructor
public @Data class EndOfDay implements Serializable { //TODO: rename to EOD
    @Id
    @SequenceGenerator(
            name = "eod_sequence",
            sequenceName = "eod_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "eod_sequence"
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
            nullable = false
    )
    @NotNull(message = "Close must not be empty.")
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
            name = "volume",
            nullable = false
    )
    @NotNull(message = "Volume must not be empty.")
    @SerializedName("volume")
    private Double volume;

    @Column(
            name = "adj_open",
            nullable = false
    )
    @NotNull(message = "Adjust open must not be empty.")
    @SerializedName("adj_open")
    private Double adjOpen;

    @Column(
            name = "adj_close",
            nullable = false
    )
    @NotNull(message = "Adjust close must not be empty.")
    @SerializedName("adj_close")
    private Double adjClose;

    @Column(
            name = "adj_high",
            nullable = false
    )
    @NotNull(message = "Adjust high must not be empty.")
    @SerializedName("adj_high")
    private Double adjHigh;

    @Column(
            name = "adj_low",
            nullable = false
    )
    @NotNull(message = "Adjust low must not be empty.")
    @SerializedName("adj_low")
    private Double adjLow;

    @Column(
            name = "adj_volume",
            nullable = false
    )
    @NotNull(message = "Adjust volume must not be empty.")
    @SerializedName("adj_volume")
    private Double adjVolume;

    @Column(
            name = "split_factor",
            nullable = false
    )
    @NotNull(message = "Split factor volume must not be empty.")
    @SerializedName("split_factor")
    private Double splitFactor;

    @Column(
            name = "date",
            nullable = false
    )
    @NotNull(message = "Date volume must not be empty.")
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
        return "EndOfDay{" +
                "id=" + id +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", adjOpen=" + adjOpen +
                ", adjClose=" + adjClose +
                ", adjHigh=" + adjHigh +
                ", adjLow=" + adjLow +
                ", adjVolume=" + adjVolume +
                ", splitFactor=" + splitFactor +
                ", date=" + date +
                '}';
    }
}