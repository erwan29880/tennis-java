package front;

/**
 * énumération des directions possibles de la balle
 */
public enum Direction {
    NE("NE"),
    SE("SE"),
    NW("NW"),
    SW("SW"),
    WN("WN"),
    WS("WS"),
    EN("EN"),
    ES("ES");

    /**
     * nom de la direction en chaîne de caractères
     */
    private final String name;       

    /**
     * constructeur
     * @param s la direction
     */
    private Direction(String s) {
        name = s;
    }

    /**
     * méthode de vérification de chaîne de caractère
     * @param otherName le nom de la direction à vérifier
     * @return la vérification
     */
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    /**
     * la direction au format String
     * @return la direction au format String
     */
    public String toString() {
        return this.name;
    }
}
