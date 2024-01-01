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
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
     * hauteur du panel des boutons
     */
    private final int controlsHeight = 70;

    /**
     * classe de gestion du chariot et de la balle
     */
    private Chariot chariot;

    
    /**
     * constructeur
     */
    public Fenetre() {
        // fenetre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(width, height);
        setLayout(null);
        setBounds(0,0, 600, 600);

        // espace de jeu
        Container c = getContentPane();

        chariot = new Chariot(height - controlsHeight);
        Controls controls = new Controls();
        c.add(controls);
        c.add(chariot);
    }



    public class Controls extends JPanel {
        
        /**
         * constructeur
         */
        public Controls() {
            setLayout(null);
            setBounds(0, 0, width, controlsHeight);
            // setBackground(Color.black);

            buttons();
        }

        protected void buttons() {
            int middle = width/2;
            int buttonWidth = 70;
            int buttonHeight = 30;
            int posY = 14;
            int ecart = 40;
            JButton start = new JButton("start");
            JButton stop = new JButton("stop");
            start.setBounds(middle - ecart - buttonWidth, posY, buttonWidth, buttonHeight);
            stop.setBounds(middle + ecart, posY, buttonWidth, buttonHeight);
            add(start);
            add(stop);
            
            start.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("start clicked"); 
                    chariot.gestionTimer();  
                }
            });

            stop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("stop clicked");   
                    chariot.stopTimer();
                }
            });
        }
    }



    /**
     * classe chariot
     */
    public class Chariot extends JPanel implements MouseMotionListener{

        protected int heigh;


        /**
         * timer pour pouvoir retracer les éléments
         */
        protected Timer timer;

        /**
         * la rapidité du timer
         */
        private final int delay = 10;
        
        /**
         * variable pour la gestion du chariot
         */
        private boolean initial = true;

        /**
         * abscisse de la balle 
         */
        protected double x = 0.0d;

        /**
         * ordonnée de la balle
         */
        protected double y = 0.0d;

        /**
         * variable qui reprend debutX de la classe Trigo
         */
        private double debutX = 0.0d;

        /**
         * variable pour afficher la direction en console uniquement à chaque changement
         */
        private int test;

        /**
         * marge en haut de la fenêtre
         */
        private final int paddingTop = 10;

        /**
         * marge en bas de la fenêtre
         */
        private final int paddingBottom = 100;

        /**
         * marge droite
         */
        private final int paddingRight = 20;

        /**
         * marge gauche
         */
        private final int paddingLeft = 25;

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
         * récupérer la direction de la balle
         */
        Direction direction;

        /**
         * classe de calcul des variables des fonctions affines
         */
        Trigo trigo;

        /**
         * constructeur
         * @param heigh hauteur de la fenêtre de jeu
         */
        public Chariot(int heigh) {

            this.heigh = heigh;

            // coordonnées du chariot
            chariotX = width/2.0d - chariotWidth/2.0d - 10;
            chariotY  = heigh - 95;

            // instanciation d'une direction pour pas d'erreur
            direction = Direction.SW; 

            // gestion de la fenêtre
            setLayout(null);
            setBounds(0, controlsHeight, width, height - controlsHeight);
            // setBackground(Color.GRAY);
            setBorder(BorderFactory.createLineBorder(Color.black));

            // gestion chariot quand la souris est cliquée
            addMouseMotionListener(this);

            // gestion de la balle
            trigo = new Trigo(width, heigh, paddingBottom);
            trigo.fromZeroToLeft(trigo.rand(0+10.0d, width - 10.0d));
            // gestionTimer(); // pour test, sinon la gestion timer est controlée par des boutons
            
        }

        /**
         * gestion de la direction de la balle en fonction d'ou elle vient 
         * gestion des rebonds sur les côtés
         * 
         * la fonction change la direction de la balle, arrête le jeu si la balle n'est pas renvoyée par le chariot
         */
        protected void gestionDirections () {
            // pour affichage console
            if (test == 0) {
                System.out.println(trigo.getRebond() + "  " + trigo.getDirection() + " " + x + "  " + y);
                test++;
            }

            // si il y a un rebond sur les côtés ou non
            switch (trigo.getRebond()) {
                case Y:
                    if (trigo.getDirection() == Direction.NW) {
                        if ((int) x <= paddingRight) {
                            trigo.fromMiddleToRight(y, false);
                            x = paddingRight;
                            test = 0;
                        } 
                    }
                    else if (trigo.getDirection() == Direction.NE) {
                        if ((int) x >= width - paddingLeft) {
                            trigo.fromMiddleToLeft(y, true);
                            x = width - paddingLeft;
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.SW) {
                        if ((int) x <= paddingRight) {
                            trigo.fromMiddleToRight(y, true);
                            x = paddingRight;
                            test = 0;
                        } 
                    }
                    else if (trigo.getDirection() == Direction.SE) {
                        if ((int) x >= width - paddingLeft) {
                            trigo.fromMiddleToLeft(y, false);
                            x = width - paddingLeft;
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.WN) {
                        if ((int) x >= width - paddingLeft) {
                            trigo.fromMiddleToRight(y, true);
                            x = width - paddingLeft;
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.EN) {
                        if ((int) x <= paddingRight) {
                            trigo.fromMiddleToRight(y, true);
                            x = paddingRight;
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.WS) {
                        if ((int) x >= width - paddingLeft) {
                            trigo.fromMiddleToLeft(y, true);
                            x = width - paddingLeft;
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.ES) {
                        if ((int) x <= paddingRight) {
                            trigo.fromMiddleToRight(y, false);
                            x = paddingRight;
                            test = 0;
                        }
                    }
                    break;
                case N:
                    if (trigo.getDirection() == Direction.NW) {
                        if ((int) y >= this.heigh - paddingBottom) {
                            checkChariotPos();
                            trigo.fromTopToLeft(x);
                            test = 0;
                        } 
                    }
                    else if (trigo.getDirection() == Direction.NE) {
                        if ((int) y >= this.heigh - paddingBottom) {
                            checkChariotPos();
                            trigo.fromTopToRight(x);
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.SW) {
                        if ((int) y <= paddingTop) {
                            trigo.fromZeroToLeft(x);
                            test = 0;
                        } 
                    }
                    else if (trigo.getDirection() == Direction.SE) {
                        if ((int) y <= paddingTop) {
                            trigo.fromZeroToRight(x);
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.WN) {
                        if ((int) y <= paddingTop) {
                            trigo.fromZeroToRight(x);
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.EN) {
                        if ((int) y <= paddingTop) {
                            trigo.fromZeroToLeft(x);
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.WS) {
                        if ((int) y >= this.heigh - paddingBottom) {
                            checkChariotPos();
                            trigo.fromTopToRight(x);
                            test = 0;
                        }
                    }
                    else if (trigo.getDirection() == Direction.ES) {
                        if ((int) y >= this.heigh - paddingBottom) {
                            checkChariotPos();
                            trigo.fromTopToLeft(x);
                            test = 0;
                        }
                    }
                    break;
            }
        }

        /**
         * gestion du timer pour bouger la balle
         */
        protected void gestionTimer() {
            if (timer != null) {
                if (timer.isRunning()) timer.stop();
            }
            timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    movePoint();
                    repaint();
                }
            });
            timer.start();
        }

        /**
         * bouger la balle
         * x est incrémenté ou décrémenté en fonction de la direction de la balle
         */
        public void movePoint() {  
            if (x == 0) {
                    x = trigo.getDebutX();
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
            gestionDirections();  
        }

        /**
         * vérifier que la balle, quand elle arrive en bas de l'écran, correspond au coordonnées du chariot
         */
        private void checkChariotPos() {
            if (x < chariotX | x > chariotX + chariotWidth) {
                System.out.println("ok");
                timer.stop();
            }
        }
    
        /**
         * affichage des éléments
         * @param g graphics
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawChariot(g);
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


        /**
         * tracer la balle
         * @param g graphics
         */
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
         * bouger le chariot au click de la souris
         * @param e event quand la souris reste cliquée
         */
        public void mouseDragged(MouseEvent e) {
            double finFenetre = width - chariotWidth - paddingRight;
            double newX = e.getX();
            if (newX >= finFenetre ) {
                chariotX = finFenetre;
            }
            else if (newX <= 10) {
                chariotX = 10.0d;
            } else {
                chariotX = newX;
            }
            this.repaint();

        }

        /**
         * implémentation de l'interface
         * @param e event
         */
        public void mouseMoved(MouseEvent e) {}

        public void stopTimer() {
            timer.stop();
        }
    }
}
