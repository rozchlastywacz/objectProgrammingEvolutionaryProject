package pl.cwikla.po.evolutionaryProject.model;

public enum Gene {
    A, B, C, D, E, F, G, H;

    public int getCode() {
        return this.ordinal();
    }

    public static Gene getByCode(int code) {
        return values()[code];
    }
}
