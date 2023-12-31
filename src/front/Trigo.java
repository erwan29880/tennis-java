package front;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Trigo {
    
    /**
     * largeur de l'espace de jeu
     */
    protected int width;
    
    /**
     * hauteur de l'espace de jeu
     */
    protected int height;

    /**
     * absisse de départ
     */
    protected double debutX;

    /**
     * angle de départ
     */
    protected int angle;

    /**
     * coefficient directeur de la droite
     */
    protected double coef;

    /**
     * le b de y = ax + b
     */
    protected double b;

    /**
     * coordonnées de fin de droite, soit [0, y quand x = 0] ou [width, y quand x = width]
     */
    protected double[] coord;

    /**
     * le cosinus de l'angle ; l'angle doit être converti en radians
     */
    protected double cosAngle; 

    /**
     * addition d'une constante en cas de division par zéro
     */
    protected final double epsilon = 1e-5d;  
    
    /**
     * la direction de la balle
     */
    protected Direction direction;
    
    /**
     * si il y a un rebond ou non
     */
    protected Rebond rebond;

    /**
     * constructeur
     * @param w la largeur de la fenêtre de jeu
     * @param h la hauteur de la fenêtre de jeu
     */
    public Trigo(int w, int h) {
        this.width = w;
        this.height = h;
        this.direction = Direction.NE;
        this.rebond = Rebond.N;

        this.initialValues();
    }

    /**
     * initialisation des variables dans une méthode séparée pour alléger le constructeur
     */
    protected void initialValues() {
        this.angle = rand(10, 80);
        this.debutX = rand(0+10.0d, this.width - 10.0d);
        this.coef = 0.0d;
        this.b = 0.0d;
        this.coord = new double[2];
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
     * trouver un nombvre pseudo-aléatoire
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
    protected double checkDivZero(double denominateur){
      return denominateur == 0 ? denominateur + epsilon : denominateur;
    }


    /**
     * modifier légèrement l'angle pour changer la direction de la droite 
     * modification de +8 ou -8 degrés
     */
    protected void modifiyAngle() {
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
     */
    protected double modifiyAngle(double ang) {
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
     */
    protected void fromZeroToRight() {
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
     */
    protected void fromZeroToLeft(){
        modifiyAngle();
        coef = - Math.tan(Math.toRadians(angle));
        b = - (coef*debutX);       
        
        direction = Direction.NW;
        toY();
    }

    /**
     * calculer les a et b en partant de l'axe des abscisse + height, retour vers l'axe des abscisses
     * a descendant 
     */
    protected void fromTopToRight () {
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
    
        direction = Direction.SE;
        toY();
    }


    /**
     * calculer a et b pour le cas ou la balle n'arrive pas à zéro
     * @param fin l'ordonnée à laquelle la trajectoire de l'axe des ordonnées
     * @boolean asc change le coefficient directeur pour que la trajectoire soit ascendante ou descendante
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
     */
    protected void fromTopToLeft () { 
        modifiyAngle();
        coef = Math.tan(Math.toRadians(angle));
        // hypothénuse
        double bo = debutX / checkDivZero(cosAngle);
        // base
        double co = Math.sqrt(Math.abs( Math.pow(bo, 2) - Math.pow(debutX, 2)));
        // thalès
        b = height - co;
        
        direction = Direction.SW;
        toY();
    }


    /**
     * calcul de x et y en fonction de x = 0 ou x = width
     * @param left en fonction de la direction de la balle
     */
    protected void calculateCoord(boolean left) {
        if (rebond == Rebond.Y) {
            coord = left == true ? new double[]{0, b} : new double[]{width, coef * width + b};
        } else {
            coord = new double[]{-99, -99};
        }
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
     * @param direction la direction à partir de l'origine
     */
    protected void toY() {
        switch(direction) {
            case NE:
                rebond = calculateY(false) >=  height ? Rebond.N : Rebond.Y;
                calculateCoord(true);
                break;
            case NW:
                rebond = calculateY(true) >= height ? Rebond.N : Rebond.Y;
                calculateCoord(false);
                break;
            case SE:
                rebond = calculateY(true) <= 0 ? Rebond.N : Rebond.Y;
                calculateCoord(true);
                break;
            case SW:
                rebond = calculateY(true) <= 0 ? Rebond.N : Rebond.Y;
                calculateCoord(false);
                break;
            case EN:
                rebond = calculateY(true) <=  0 ? Rebond.N : Rebond.Y;
                calculateCoord(true);
                break;
            case ES:
                rebond = calculateY(true) >= height ? Rebond.N : Rebond.Y;
                calculateCoord(true);
                break;
            case WN:
                rebond = calculateY(false) <= 0 ? Rebond.N : Rebond.Y;
                calculateCoord(false);
                break;
            case WS:
                rebond = calculateY(false) >= height ? Rebond.N : Rebond.Y;
                calculateCoord(false);
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

    public boolean[] testDirections() {
        double coeff = coef;
        double bb = b;
        boolean[] checks = new boolean[4];
        fromZeroToRight();
        checks[0] = coef != coeff | b != bb;
        fromTopToLeft();
        checks[1] = coef != coeff | b != bb;
        fromTopToRight();
        checks[2] = coef != coeff | b != bb;
        fromTopToLeft();
        checks[3] = coef != coeff | b != bb;
        return checks;
    }

    protected double getCoef() {
        return coef;
    }

    protected double getB() {
        return b;
    }

    protected double getDebutX() {
        return debutX;
    }

    protected Direction getDirection() {
        return direction;
    }

    protected Rebond getRebond() {
        return rebond;
    }

    protected double[] getCoord() {
        return coord;
    }
}
