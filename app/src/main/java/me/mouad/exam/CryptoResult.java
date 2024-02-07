package me.mouad.exam;

public class CryptoResult {

    private final double usd, eur, mad;

    public CryptoResult(double usd, double eur, double mad) {
        this.usd = usd;
        this.eur = eur;
        this.mad = mad;
    }

    public double getUsd() {
        return usd;
    }

    public double getEur() {
        return eur;
    }

    @Override
    public String toString() {
        return "CryptoResult{" +
                "usd=" + usd +
                ", eur=" + eur +
                ", mad=" + mad +
                '}';
    }

    public double getMad() {
        return mad;
    }
}
