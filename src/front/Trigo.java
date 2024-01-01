package front;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * La classe Trigo calcule la trajectoire de la balle en fonction de sa position. 
 * La trajectoire de la balle est définie selon un angle de départ, et un coefficient directeur.
 * Cet angle est modifié à chaque rebond. 
 * Le coefficient directeur est modifié en fonction de la pente ascendante ou descendante de la balle.  
 * Le "b" de y = ax + b est recalculé pour chaque trajectoire.
 * @author erwan tanguy
 */
class Trigo {
    
    /**
     * largeur de l'espace de jeu
     */
    private int width;
    
    /**
     * hauteur de l'espace de jeu
     */
    private int height;

    /**
     * absisse de départ
     */
    private double debutX;

    /**
     * angle de départ
     */
    private int angle;

    /**
     * coefficient directeur de la droite
     */
    private double coef;

    /**
     * le b de y = ax + b
     */
    private double b;

    /**
     * le cosinus de l'angle ; l'angle doit être converti en radians
     */
    private double cosAngle; 

    /**
     * addition d'une constante en cas de division par zéro
     */
    private final double epsilon = 1e-5d;  
    
    /**
     * la direction de la balle
     */
    private Direction direction;
    
    /**
     * si il y a un rebond ou non
     */
    private Rebond rebond;

    /**
     * le padding de la fenêtre pour le calcul de b
     */
    private int paddingBottom;


    /**
     * constructeur
     * @param w la largeur de la fenêtre de jeu
     * @param h la hauteur de la fenêtre de jeu
     * @param padd le padding bottom de la fenêtre
     */
    Trigo(int w, int h, int padd) {
        this.width = w;
        this.height = h;
        this.direction = Direction.NE;
        this.rebond = Rebond.N;
        this.paddingBottom = padd;
        this.initialValues();
    }

    /**
     * initialisation des variables dans une méthode séparée pour alléger le constructeur
     */
    private void initialValues() {
        this.angle = rand(70, 80);
        this.debutX = rand(0+10.0d, this.width - 10.0d);
        this.coef = 0.0d;
        this.b = 0.0d;
        this.cosAngle = 0.0d;
    }

    /**
     * trouver un nombvre pseudo-aléatoire
     * @param deb le minimum du nombre aléatoire
     * @param w le maximum du nombre aléatoire
     * @return un nombre pseudo aléatoire en tre deb et w
     */
    protected int rand(int deb, int w) {
        return (int) (deb + Math.random()*(w-deb));
    }

    /**
     * trouver un nombre pseudo-aléatoire
     * @param deb le minimum du nombre aléatoire
     * @param w le maximum du nombre aléatoire
     * @return un nombre pseudo aléatoire en tre deb et w
     */
    protected double rand(double deb, double w) {
        return deb + Math.random()*(w-deb);
    }


    /**
     * ajouter un petit nombre au dénominateur en cas de dénominateur = 0
     * @param denominateur le dénominateur de la division
     * @return le dénominateur non égal à zéro
     */
    private double checkDivZero(double denominateur){
      return denominateur == 0 ? denominateur + epsilon : denominateur;
    }


    /**
     * modifier légèrement l'angle pour changer la direction de la droite 
     * modification de +8 ou -8 degrés
     */
    private void modifiyAngle() {
        // modification aléatoire de la valeur de l'angle
        int alea = rand(0, 8);
        // pour additioner ou soustraire
        Random r = new Random();
        boolean alea2 = r.nextBoolean();
    
        if (angle >= 80) {
            angle = angle - alea;
        } else if (angle <= 10) {
            angle = angle + alea;
        } else {
            angle = alea2 == true ? angle + alea : angle - alea;
        } 
    
        // recalcul du cosinus
        cosAngle = Math.cos(Math.toRadians(angle));
    }

    /**
     * modifier légèrement l'angle pour changer la direction de la droite 
     * pour tests
     * modification de +8 ou -8 degrés
     * @param ang l'angle à modifier
     */
    private double modifiyAngle(double ang) {
        // modification aléatoire de la valeur de l'angle
        int alea = rand(0, 8);
        // pour additioner ou soustraire
        Random r = new Random();
        boolean alea2 = r.nextBoolean();
    
        if (ang >= 80) {
            return ang - alea;
        } else if (ang <= 10) {
            return  ang + alea;
        } else {
            return alea2 == true ? ang + alea : ang - alea;
        }     
    }


    /**
     * calculer les a et b en partant de l'axe des abscisse
     * a ascendant 
     * @param absc l'absisse pour calculer b
     */
    protected void fromZeroToRight(double absc) {
        debutX = absc;
        modifiyAngle();
        double hypo = width/checkDivZero(cosAngle);
        double coteOpp = Math.sqrt(Math.abs(Math.pow(hypo, 2) - Math.pow(width,2) ));        
        coef = Math.tan(Math.toRadians(angle));
        b = debutX * coteOpp / width;
        if (debutX > 0) {
            b = -b;
        }
    
        direction = Direction.NE;
        toY();
    }

    /**
     * calculer les a et b en partant de l'axe des abscisse
     * a descendant 
     * @param absc l'absisse pour calculer b
     */
    protected void fromZeroToLeft(double absc){
        debutX = absc;
        modifiyAngle();
        coef = - Math.tan(Math.toRadians(angle));
        b = - (coef*debutX);       
        
        direction = Direction.NW;
        toY();
    }

 
    /**
     * calculer a et b pour le cas ou la balle n'arrive pas à zéro
     * @param fin l'ordonnée à laquelle la trajectoire de l'axe des ordonnées
     * @param asc change le coefficient directeur pour que la trajectoire soit ascendante ou descendante
     */
    protected void fromMiddleToRight (double fin, boolean asc) {
        modifiyAngle();
        coef = Math.tan(Math.toRadians(angle));
        if (asc == true) {
            coef = - coef;
        }
        b = fin;

        direction = asc == true ? Direction.WN : Direction.WS;
        toY();
    }

    /**
     * calculer a et b pour le cas ou la balle n'arrive pas à zéro
     * @param fin l'ordonnée à laquelle la trajectoire de l'axe des ordonnées
     * @param asc coefficient directeur ascendant ou descendant
     */
    protected void fromMiddleToLeft (double fin, boolean asc) {
        modifiyAngle();
        coef = Math.tan(Math.toRadians(angle));
        if (asc == true) {
            coef = - coef;
        }
       
        b = fin - (width * coef);

        direction = asc == true ? Direction.ES : Direction.EN;
        toY();
    }


    /**
     * calculer les a et b en partant de l'axe des abscisse + height, retour vers l'axe des abscisses
     * a ascendant 
     * @param absc l'absisse pour le calcul de b
     */
    protected void fromTopToLeft (double absc) { 
        debutX = absc;
        modifiyAngle();
        coef = Math.tan(Math.toRadians(angle));
        double bo = debutX / checkDivZero(cosAngle);  // hypothénuse
        double co = Math.sqrt(Math.abs( Math.pow(bo, 2) - Math.pow(debutX, 2))); // base
        b = height - co; // thales
        b = b - paddingBottom;

        direction = Direction.SW;
        toY();
    }

    /**
     * calculer les a et b en partant de l'axe des abscisse + height, retour vers l'axe des abscisses
     * a descendant 
     * @param absc l'absisse pour le calcul de b
     */
    protected void fromTopToRight (double absc) {
        debutX = absc;
        modifiyAngle();
        // angle inversé !
        double cosAng = Math.cos(Math.toRadians(90-angle));
        // coefficient négatif
        coef = -Math.tan(Math.toRadians(angle));
        // calcul hypothénuse
        double oz = height / checkDivZero(cosAng);
        // calcul base
        double xz = Math.sqrt(Math.abs(Math.pow(oz, 2) - Math.pow(height, 2)));
        // calcul absisse totale
        double az = xz + debutX; 
        // thalès
        b = az * height / checkDivZero(xz); 
        b = b - paddingBottom;
        direction = Direction.SE;
        toY();
    }


    /**
     * calcul de y
     * @param left si left est vrai, x est égal à 0
     * @return la coordonnée y en fonction de x = 0 ou x = width
     */
    protected double calculateY(boolean left) {
        return left == true ? b : coef * width + b;
    }


    /**
     * calcul des coordonnées de y si x = 0 ou x = width selon la direction
     * calcul si il y a un rebond sur l'axe des absisses ou non
     */
    private void toY() {
        switch(direction) {
            case NE:
                rebond = calculateY(false) >= height ? Rebond.N : Rebond.Y;
                break;
            case NW:
                rebond = calculateY(true) >= height ? Rebond.N : Rebond.Y;
                break;
            case SE:
                rebond = calculateY(false) <= 0 ? Rebond.N : Rebond.Y;
                break;
            case SW:
                rebond = calculateY(true) <= 0 ? Rebond.N : Rebond.Y;
                break;
            case EN:
                rebond = calculateY(true) <=  0 ? Rebond.N : Rebond.Y;
                break;
            case ES:
                rebond = calculateY(true) >= height ? Rebond.N : Rebond.Y;
                break;
            case WN:
                rebond = calculateY(false) <= 0 ? Rebond.N : Rebond.Y;
                break;
            case WS:
                rebond = calculateY(false) >= height ? Rebond.N : Rebond.Y;
                break;
        }
    }


    /**
     * méthode publique pour tester la fonction de choix de nombre aléatoire
     * @return false en cas d'échec
     */
    public boolean testRand() {
        boolean check = true;
        for (int i = 0; i < 1000 ;i++) {
            int t = rand(10, 90);
            if (t <= 10 & t >= 90) {
                return false;
            }
        }
        return check;
    }

    /**
     * méthode pour tester la méthode checkDivZero()
     * @return retourne normalement 0 + this.espilon
     */
    public double testDenominateur() {
        double nbTest = 0.0d;
        return checkDivZero(nbTest);
    }

    /**
     * méthode de test
     * @return une liste de booléens qui indiquent si les tests sont concluants
     */
    public List<Boolean> testModifiyAngle() {
        List<Double> angles = Arrays.asList(11.0d, 50.0d, 79.0d);
        List<Double> anglesModifies = new ArrayList<Double>();
        List<Boolean> checks = new ArrayList<Boolean>();
        angles.stream().map(c -> modifiyAngle(c)).forEach(anglesModifies::add);
        checks.add(anglesModifies.get(0) <= 19);
        checks.add((anglesModifies.get(1) >= 42 & anglesModifies.get(1) <= 58));
        checks.add(anglesModifies.get(2) >= 71);
        return checks;
    }


    /**
     * méthode de test
     * @return une liste de booléens en fonction du succès des tests
     */
    public boolean[] testDirections() {
        double coeff = coef;
        double bb = b;
        boolean[] checks = new boolean[4];
        fromZeroToRight(100);
        checks[0] = coef != coeff | b != bb;
        fromTopToLeft(100);
        checks[1] = coef != coeff | b != bb;
        fromTopToRight(100);
        checks[2] = coef != coeff | b != bb;
        fromTopToLeft(100);
        checks[3] = coef != coeff | b != bb;
        return checks;
    }

    /**
     * getter pour le coefficient directeur
     * @return le coefficient directeur
     */
    protected double getCoef() {
        return coef;
    }

    /**
     * getter b
     * @return le b de ax + b
     */
    protected double getB() {
        return b;
    }

    /**
     * getter debutX
     * @return l'absisse de départ
     */
    protected double getDebutX() {
        return debutX;
    }

    /**
     * getter direction
     * @return la direction de la balle
     */
    protected Direction getDirection() {
        return direction;
    }

    /**
     * getter rebond ou non
     * @return Y ou N
     */
    protected Rebond getRebond() {
        return rebond;
    }

    /**
     * getter pour l'angle
     * @return l'angle
     */
    protected int getAngle() {
        return angle;
    }
}
