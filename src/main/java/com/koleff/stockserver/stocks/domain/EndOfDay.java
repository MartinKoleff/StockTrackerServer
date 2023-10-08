package com.koleff.stockserver.stocks.domain;

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
public @Data class EndOfDay implements Serializable { //rename to EOD
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
    private Long id;

    @Column(
            name = "stock_id",
            nullable = false
    )
    private Long stockId;


    @Column(
            name = "open",
            nullable = false
    )
    private Double open;

    @Column(
            name = "close",
            nullable = false
    )
    private Double close;

    @Column(
            name = "high",
            nullable = false
    )
    private Double high;

    @Column(
            name = "low",
            nullable = false
    )
    private Double low;

    @Column(
            name = "volume",
            nullable = false
    )
    private Double volume;

    @Column(
            name = "adj_open",
            nullable = false
    )
    private Double adjOpen;

    @Column(
            name = "adj_close",
            nullable = false
    )
    private Double adjClose;

    @Column(
            name = "adj_high",
            nullable = false
    )
    private Double adjHigh;

    @Column(
            name = "adj_low",
            nullable = false
    )
    private Double adjLow;

    @Column(
            name = "adj_volume",
            nullable = false
    )
    private Double adjVolume;

    @Column(
            name = "split_factor",
            nullable = false
    )
    private Double splitFactor;

    @Column(
            name = "date",
            nullable = false
    )
    private Date date;

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