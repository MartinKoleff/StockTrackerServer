package com.koleff.stockserver.stocks.domain;

import jakarta.persistence.*;

import java.sql.Date;

@Entity(name = "EndOfDay")
@Table(name = "end_of_day")
public class EndOfDay { //rename to EOD
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

    public EndOfDay(Long id, Long stockId, Double open, Double close, Double high, Double low, Double volume, Double adjOpen, Double adjClose, Double adjHigh, Double adjLow, Double adjVolume, Double splitFactor, Date date) {
        this.id = id;
        this.stockId = stockId;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.adjOpen = adjOpen;
        this.adjClose = adjClose;
        this.adjHigh = adjHigh;
        this.adjLow = adjLow;
        this.adjVolume = adjVolume;
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

    public Double getAdjOpen() {
        return adjOpen;
    }

    public void setAdjOpen(Double adjOpen) {
        this.adjOpen = adjOpen;
    }

    public Double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(Double adjClose) {
        this.adjClose = adjClose;
    }

    public Double getAdjHigh() {
        return adjHigh;
    }

    public void setAdjHigh(Double adjHigh) {
        this.adjHigh = adjHigh;
    }

    public Double getAdjLow() {
        return adjLow;
    }

    public void setAdjLow(Double adjLow) {
        this.adjLow = adjLow;
    }

    public Double getAdjVolume() {
        return adjVolume;
    }

    public void setAdjVolume(Double adjVolume) {
        this.adjVolume = adjVolume;
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