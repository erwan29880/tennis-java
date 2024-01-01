package front;

/**
 * pour savoir si il y aura un rebond sur les côtés
 */
public enum Rebond {
    Y("rebond"),
    N("pas de rebond");
    

    /**
     * rebond ou non en chaîne de caractères
     */
    private final String name;       

    /**
     * constructeur
     * @param s rebon ou non 
     *  
     */ 
    private Rebond(String s) {
        name = s;
    }

    /**
     * méthode de vérification de chaîne de caractère
     * @param otherName "pas de rebond", "rebond"
     * @return la vérification
     */
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    /**
     * rebond ou non au format String
     * @return rebond ou non au format String
     */
    public String toString() {
        return this.name;
    }
}
