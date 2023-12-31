package front;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;

/**
 * classe de la fenêtre
 */
public class Fenetre extends JFrame {
    
    /**
     * largeur de la fenetre
     */
    private final int width = 600;

    /**
     * hauteur de la fenetre
     */
    private final int height = 600;

    /**
     * largeur du chariot
     */
    private final double chariotWidth = 50.0d;

    /** 
     * hauteur du chariot 
     */
    private final double chariotHeight = 15.0d;

    /**
     * cordoonées x chariot
     */
    private volatile double chariotX;

    /**
     * cordoonées y chariot
     */
    private double chariotY;


    /**
     * constructeur
     */
    public Fenetre() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(width, height);
        setLayout(null);
        setBounds(0,0, 600, 600);
        Container c = getContentPane();
        Chariot chariot = new Chariot();
        c.add(chariot);
    }



    /**
     * classe chariot
     */
    public class Chariot extends JPanel implements MouseMotionListener{

        protected Timer timer;
        private int delay;
        protected long start;
        private boolean initial = true;
        protected double x = 0.0d;
        protected double y = 0.0d;
        private double debutX = 0.0d;
        private double debutY = 0.0d;
        private int paddingRight = 20;
        private int paddingLeft = 10;
        private double initialAngle = Math.toRadians(80); 
        private double hypo;
        private double coefDirecteur;
        private double b;
        Trigo trigo;
        Direction direction;

        /**
         * constructeur
         */
        public Chariot() {
            delay = 30;
            chariotX = width/2.0d - chariotWidth/2.0d - 10;
            chariotY  = height - 80;
            direction = Direction.SW;
            setLayout(null);
            setBounds(0, 0, width-20, height-20);
            addMouseMotionListener(this);
            trigo = new Trigo(width-20, height-20);
            trigo.fromTopToRight();
            // gestionTimer();
            while(true) {
                gestionDirections();
            }
        }

        protected boolean gestionDirections () {
            if (timer == null) return false;
            if (timer.isRunning()) return false;
            switch (trigo.getRebond()) {
                case Y:
                    switch(direction) {
                        case NE:
                            trigo.fromMiddleToLeft(y, true);
                            break;
                        case NW:
                            trigo.fromMiddleToRight(y, true);
                            break;
                        case SE:
                            trigo.fromMiddleToLeft(y, false);
                            break;
                        case SW:
                            trigo.fromMiddleToRight(y, false);
                            break;
                        case EN:
                            trigo.fromTopToRight();
                            break;
                        case ES:
                            trigo.fromTopToRight();
                            break;
                        case WN:
                            trigo.fromTopToRight();
                            break;
                        case WS:
                            trigo.fromTopToRight();
                            break;
                        }
                        x = 0;
                        y = 0;
                    break;
                case N:
                    switch(direction) {
                        case NE:
                            trigo.fromTopToRight();
                            break;
                        case NW:
                            trigo.fromTopToLeft();
                            break;
                        case SE:
                            trigo.fromZeroToRight();
                            break;
                        case SW:
                            trigo.fromZeroToLeft();
                            break;
                        case EN:
                            break;
                        case ES:
                            break;
                        case WN:
                            break;
                        case WS:
                            break;
                    }
                    x = 0;
                    y = 0;
                    break;
            }
            gestionTimer();
            return true;
        }

        protected void gestionTimer() {
            if (timer != null) {
                if (timer.isRunning()) timer.stop();
            }
            
            // trigo.fromMiddleToLeft(300, false); // test ok
            // trigo.fromMiddleToRight(300, true); // test ok
            // trigo.fromTopToLeft(); // test ok
            // trigo.fromTopToRight(); // test ok
            // trigo.fromZeroToLeft(); // test ok
            
            
            timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    movePoint();
                    

                    if (trigo.getRebond() == Rebond.Y) {
                        if (trigo.getDirection() == Direction.WS | 
                            trigo.getDirection() == Direction.WN |
                            trigo.getDirection() == Direction.SE |
                            trigo.getDirection() == Direction.NE) {
                            if ((int) x >= width - 40) {
                                timer.stop();
                                System.out.println("stopped1-1");
                            }
                        }

                        if (trigo.getDirection() == Direction.ES | 
                            trigo.getDirection() == Direction.EN |
                            trigo.getDirection() == Direction.SW |
                            trigo.getDirection() == Direction.NW) {
                            if ((int) x <= 40) {
                                timer.stop();
                                System.out.println("stopped1-2");
                            }
                        }
                    } else {
                        if (trigo.getDirection() == Direction.WN | 
                            trigo.getDirection() == Direction.EN |
                            trigo.getDirection() == Direction.SW |
                            trigo.getDirection() == Direction.SE) {
                            System.out.println("direction vers nord se pas de rebond");
                            if ((int) y <= 40) {
                                timer.stop();
                                System.out.println("stopped2-1");
                            }
                        } else {
                            System.out.println("direction vers sud pas de rebond");
                            if ((int) y >= height - 40) {
                                timer.stop();
                                System.out.println("stopped2-2");
                            }
                        }
                    }
                    repaint();
                }

            });
            timer.start();
        }


        public void movePoint() {  
            // System.out.println(trigo.getDirection());
            // System.out.println(trigo.getRebond());
            if (x == 0) {

                if (trigo.getDirection() == Direction.EN |
                    trigo.getDirection() == Direction.ES) {
                        x = width - 40;
                } else if (trigo.getDirection() == Direction.WN |
                    trigo.getDirection() == Direction.WS) {
                        x = 0 + 40;
                } else {
                    x = trigo.getDebutX();
                }

                // si middle
                // x = width;
                // x++;
            } else {
                if (trigo.getDirection() == Direction.NE | 
                    trigo.getDirection() == Direction.SE |
                    trigo.getDirection() == Direction.WN |
                    trigo.getDirection() == Direction.WS) {
                    this.x++;
                } else {
                    this.x--;
                }
            }
            y = trigo.getCoef() * x + trigo.getB();

            // System.out.println(x + "  " + y);
        }
    
        /**
         * affichage des éléments
         * @param g graphics
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // drawChariot(g);
            drawLine(g);
        }


        /**
         * @param g graphics
         */
        private void drawChariot(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;  // parsage en 2d pour utilisation de double pour affichage texte 
            Rectangle2D.Double rect = new Rectangle2D.Double(chariotX, chariotY, chariotWidth, chariotHeight);
            g2.setPaint(Color.RED);
            g2.fill(rect);
        }





        private void drawLine(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (initial) {
                x = debutX;
                y = 0;
                initial = !initial;
            } 
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(4));
            g2.draw(new Line2D.Double(x, y, x, y));
    
        }

        /**
         * @param e event
         */
        public void mouseDragged(MouseEvent e) {
            double finFenetre = width - chariotWidth - 25;
            double newX = e.getX();
            if (newX >= finFenetre ) {
                chariotX = finFenetre;
            }
            else if (newX <= 10) {
                chariotX = 10.0d;
            } else {
                chariotX = newX;
            }
            // drawLine(g);
            this.repaint();

        }

        /**
         * implémentation de l'interface
         * @param e event
         */
        public void mouseMoved(MouseEvent e) {}

    }
}
