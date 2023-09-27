package com.koleff.stockserver.stocks.domain;

import jakarta.persistence.*;

import java.sql.Date;

@Entity(name = "IntraDay")
@Table(name = "intra_day")
public class IntraDay {
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

    @ManyToOne
//    @MapsId("id")
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

    public IntraDay(Long id, Double open, Double close, Double high, Double low, Double volume, Double splitFactor, Date date) {
        this.id = id;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.splitFactor = splitFactor;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getSplitFactor() {
        return splitFactor;
    }

    public void setSplitFactor(Double splitFactor) {
        this.splitFactor = splitFactor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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