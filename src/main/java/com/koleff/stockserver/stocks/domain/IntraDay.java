package com.koleff.stockserver.stocks.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

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
        return "IntraDay{" +
                "id=" + id +
                ", stockId=" + stockId +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", splitFactor=" + splitFactor +
                ", date=" + date +
                '}';
    }
}