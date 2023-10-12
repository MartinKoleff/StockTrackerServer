package com.koleff.stockserver.stocks.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Entity(name = "EndOfDay")
@Table(name = "end_of_day")
@NoArgsConstructor
@AllArgsConstructor
public @Data class EndOfDay implements Serializable, SupportTable { //rename to EOD
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
    @Expose(deserialize = false)
    @SerializedName("stock_id")
    private Long stockId;


    @Column(
            name = "open",
            nullable = false
    )
    @SerializedName("open")
    private Double open;

    @Column(
            name = "close",
            nullable = false
    )
    @SerializedName("close")
    private Double close;

    @Column(
            name = "high",
            nullable = false
    )
    @SerializedName("high")
    private Double high;

    @Column(
            name = "low",
            nullable = false
    )
    @SerializedName("low")
    private Double low;

    @Column(
            name = "volume",
            nullable = false
    )
    @SerializedName("volume")
    private Double volume;

    @Column(
            name = "adj_open",
            nullable = false
    )
    @SerializedName("adj_open")
    private Double adjOpen;

    @Column(
            name = "adj_close",
            nullable = false
    )
    @SerializedName("adj_close")
    private Double adjClose;

    @Column(
            name = "adj_high",
            nullable = false
    )
    @SerializedName("adj_high")
    private Double adjHigh;

    @Column(
            name = "adj_low",
            nullable = false
    )
    @SerializedName("adj_low")
    private Double adjLow;

    @Column(
            name = "adj_volume",
            nullable = false
    )
    @SerializedName("adj_volume")
    private Double adjVolume;

    @Column(
            name = "split_factor",
            nullable = false
    )
    @SerializedName("split_factor")
    private Double splitFactor;

    @Column(
            name = "date",
            nullable = false
    )
    @SerializedName("date")
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "stock_id",
            nullable = false,
            insertable=false,
            updatable=false,
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
                ", stockId=" + stockId +
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