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
        protected double x = 0;
        protected double y = 0;
        private double debutX = 0;
        private double debutY = 0;
        private int paddingRight = 20;
        private int paddingLeft = 10;
        private double initialAngle = Math.toRadians(80); 
        private double hypo;
        private double coefDirecteur;
        private double b;

        /**
         * constructeur
         */
        public Chariot() {
            delay = 30;
            chariotX = width/2.0d - chariotWidth/2.0d - 10;
            chariotY  = height - 80;
            setLayout(null);
            setBounds(0, 0, width, height);
            addMouseMotionListener(this);
            gestionTimer();
            findCoordinates();
        }

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


        private void findCoordinates () {
            double w = Math.abs(width - paddingLeft - x);
            hypo =  w / Math.cos(initialAngle);
            coefDirecteur =  Math.tan(initialAngle);
            double yfin = Math.sqrt( Math.pow(hypo, 2) - Math.pow(w, 2) );
            b = yfin - coefDirecteur * w;
        }


        public void movePoint() {
            findCoordinates();
            x = x <= width ? x+1 : x-1;
            y = coefDirecteur * x + b;
            if (y >= height - 80) {
                initialAngle =  - initialAngle;
            }
            System.out.println(x + " " + y);
            // y = coefDirecteur * x + b;
            // if (x == width - paddingLeft | x == 0 | (int) y == 0 |  y >= height - chariotHeight ) {
            //     timer.stop();
            //     System.out.println("timer stopped");
            // }
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

        private void drawLine(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            if (initial) {
                x = debutX;
                y = debutY;
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
