/*
 * Juego tipo brick breaker en el que el jugador debe de mover la plataforma
 * con las flechas para poder hacer rebotar un proyectil y destruir las anfetaminas
 */
package breakingbad;

import java.applet.AudioClip;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 *
 * @autores Jose Gonzalez - A01036121
 *          Edna Aguirre - A00813646
 * Fecha: 25/02/15
 */
public class BreakingBad extends JFrame implements Runnable, KeyListener {
    
    private int iCantAnfetaminas; // cantidad de anfetaminas
    
    private AudioClip aucColision; // objeto de audio para la colision
    private int iPuntos; // puntuacion del usuario
    
    private Animacion animProyectil; // animacion para el proyectil
    private boolean bColisionPildora; // booleana para registrar las colisiones
    
    private Base basBarra; // objeto de la barra
    private Base basPildora; // objeto de la pildora

    private LinkedList<Base> lklPildoras; // coleccoin de pildoras
    
    private boolean bGameOver; // booleana para la finalizacion del juego
    private boolean bPausa;    // booleana para controlar la pausa
    
    // constantes para dividir la ventana uniformemente
    public static final int iMAXANCHO = 8;
    public static final int iMAXALTO = 8;

    private Graphics graGraficaFrame;  // objeto grafico de la Imagen
    private Image imaImagenFrame;   // imagen a proyectar en Frame	
    
    private int iDireccion; // direccion en la que se mueve la barra
    
    private boolean bTeclaPresionada; // booleana que revisa si hay alguna tecla presoinada
    
    private final int iVelocidadBarra; // velocidad de movimiento de la barra
    private int iVelocidadProyectilY; // velocidad en Y de la barra
    private int iVelocidadProyectilX; // velocidad en X de la barra

    private long lTiempoActual; // tiempo de control de la animacion
    
    private Base basProyectil; // objero del proyectil
        
    private int iVidas; // las vidas del jugador 
    
    private Image imgGameOver; // imagen del fin del juego cuando se pierde
    private Image imgWellDone; // imagen del fin del juego cuando se gana

    /**
     * BreakingBad (constructor)
     *
     * Constructor de la clase <code>CuartaTarea.java</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse y se definen funcionalidades.
     *
     */
     public BreakingBad () {
        
        iCantAnfetaminas = 23;
        iPuntos = 0; // comienza con cero puntos
        iVidas = 5; // el jugador comienza con cinco vidas
        bColisionPildora = false; // al comienzo no hay contacto
        
        iVelocidadProyectilY = 0; // la velocidad en y comienza en cero
        iVelocidadProyectilX = 5; // la velocidad en x comienza en cinco
         
        lklPildoras = new LinkedList(); // inicializa la coleccion de pildoras
         
        iVelocidadBarra = 10; // la velocidad de la barra se establece en diez
        iDireccion = 0; // la barra comienza sin moverse
        
        bTeclaPresionada = false; // comienza sin ninguna tecla presionada
        bGameOver = false; // el juego comienza
        bPausa = true; // el juego comienza sin pausa
        
        // imagen del fin del juego cuando se pierde
        URL urlImagenGameOver = this.getClass().getResource("gameOver.jpg");
        imgGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);
        
        // imagen del fin del juego cuando se gana
        URL urlImagenWellDone = this.getClass().getResource("wellDone.jpg");
        imgWellDone = Toolkit.getDefaultToolkit().getImage(urlImagenWellDone);
        
        // hago el frame de un tamaño 500,500
        setSize(800,500);
        
        // carga la imagen de la barra
	URL urlImagenBarra = this.getClass().getResource("barraPrueba.png");
        
        // imagenes para la animacion del proyectil
        URL urlImagenProyectil1 = this.getClass().getResource("bala1.png");
        URL urlImagenProyectil2 = this.getClass().getResource("bala2.png");
        URL urlImagenProyectil3 = this.getClass().getResource("bala3.png");
        URL urlImagenProyectil4 = this.getClass().getResource("bala4.png");
        URL urlImagenProyectil5 = this.getClass().getResource("bala5.png");
        URL urlImagenProyectil6 = this.getClass().getResource("bala6.png");
        URL urlImagenProyectil7 = this.getClass().getResource("bala7.png");
        URL urlImagenProyectil8 = this.getClass().getResource("bala8.png");
        
        // agrega las imagenes a la secuencia de animacion
        animProyectil = new Animacion();
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil1), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil2), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil3), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil4), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil5), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil6), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil7), 100);
        animProyectil.sumaCuadro(Toolkit.getDefaultToolkit().getImage(urlImagenProyectil8), 100);
        
        // se crea el objeto para la barra
	basBarra = new Base(0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenBarra));

        // se posiciona la barra en la mitad abajo en el Frame 
        basBarra.setX((getWidth() / 2) - (basBarra.getAncho() / 2));
        basBarra.setY(getHeight() - 2 * basBarra.getAlto());
        
        // se crea el objeto para el proyectil
	basProyectil = new Base(0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenProyectil1));

        // se posiciona al proyectil encima de la barra en la mitad derecha
        basProyectil.setX(basBarra.getX() + basBarra.getAncho() - (basBarra.getAncho() / 4));
        basProyectil.setY(basBarra.getY() - basBarra.getAlto());
        
        URL urlSonidoColision = this.getClass().getResource("explosion-02.wav");
        aucColision = Applet.newAudioClip(urlSonidoColision);
        
        // genera anfetaminas
        generarAnfetaminas();
        
        // el keylistener es esta clase
        addKeyListener(this);
        
        // declaras un hilo
        Thread th = new Thread (this);
        // empieza el hilo
        th.start ();
    
    }
     
    /**
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo, si el juego es pausado
           se detiene el movimiento, pero si se termina se deja de dibujar
           tambien.
        */
        
        // tiempo actual del sistema
        lTiempoActual = System.currentTimeMillis();
        
        while (!bGameOver) { // si no ha terminado el juego
             if (!bPausa) { // y si no hay pausa
                actualiza(); // se actualiza
                checaColision(); // y se checan las colisiones
             }
            repaint(); // de lo contrario se deja de pintar
            try	{
                // el thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
        }  
    }   
     
     /** 
     * actualiza()
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza() {
        
         // determina el tiempo que ha transcurrido desde que el JFrame inicio su ejecución
         long lTiempoTranscurrido = System.currentTimeMillis() - lTiempoActual;
            
         // guarda el tiempo actual
       	 lTiempoActual += lTiempoTranscurrido;
       	 
       	 // actualiza la animación del elefante en base al tiempo transcurrido
       	 animProyectil.actualiza(lTiempoTranscurrido);
		
        // si las vidas llegan a cero o bien se eliminan todas las capsulas 
        if (iVidas <= 0 || iPuntos == iCantAnfetaminas ) {
            // termina el juego
            bGameOver = !bGameOver;
             /* se posiciona a principal  en la esquina superior izquierda del Frame 
                o si no el proyectil seguira cayengo restando vidas
             */
            basProyectil.setX(basBarra.getX() + basBarra.getAncho() 
                    - (basBarra.getAncho() / 4));
        
            basProyectil.setY(basBarra.getY() - basBarra.getAlto());
            
        }
        
        // movimiento de la barra
        if (bTeclaPresionada) {
            // dependiendo de la direccion
            switch(iDireccion) {
                case 1: // se mueve hacia la izquierda
                    basBarra.setX(basBarra.getX() - iVelocidadBarra);
            
                    break;
                case 2: // se mueve hacia la derecha
                     basBarra.setX(basBarra.getX() + iVelocidadBarra);
                    break;
                default:
                    break;          
            }   
        }
        // el proyectil se mueve constantemente
        basProyectil.setX(basProyectil.getX() + iVelocidadProyectilX);
        basProyectil.setY(basProyectil.getY() - iVelocidadProyectilY);
        
    }
   /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * |                        
     **/
    public void checaColision() {
        // variable para obtener objetos dentro de la colieccion de pildoras
        int iIndex = 0;
        
        // para cada pildora en la coleccion
        for (Base basPildora2 : lklPildoras) {
            // si la pildora colisiona con el proyectil
            if (basPildora2.intersecta(basProyectil)) {
                // emite sonido de colision
                aucColision.play();
                // la direccion del proyectil se invierte
                iVelocidadProyectilY *= -1;
                iVelocidadProyectilX *= -1;
                // se registra que se ha llevado a cabo una colision
                bColisionPildora = true;
                // se guarda la posicion de la pildora que recibio el impacto
                iIndex = lklPildoras.indexOf(basPildora2);
            }
        }
        // si se ha impactado con una pildora
        if (bColisionPildora) {
            // la puntuacion aumenta
            iPuntos ++;
            // se elimina la pildora colisionada de la coleccion 
            lklPildoras.remove(iIndex);
            // la booleana regresa a falso
            bColisionPildora = false;
        }
        
        // si el proyectil colisiona con la barra
        if (basProyectil.intersecta(basBarra)) {
            
            // si la colision se da en la mitad izquierda de la barra
            if (basProyectil.getX() >= (basBarra.getX() + basBarra.getAncho()/2)) {
                // y si la velocidad del proyectil es menor a cero (viene desde izquierda)
                if (iVelocidadProyectilX <= 0) {
                    // entonces la velocidad en x se invierte
                    iVelocidadProyectilX *= -1;
                }
            } 
            else { // de lo contrario
                // la velocidad se invierte al otro lado
                if (iVelocidadProyectilX <= 0) {
                    iVelocidadProyectilX *= 1;
                } 
                else {
                    iVelocidadProyectilX *= -1;
                }
            }
            // la inclinacion del proyectil depende de la region de colision con la barra
            iVelocidadProyectilY = (basBarra.getX() - basBarra.getAncho()/4)/100 + 10;
        }
        
        // colision con pared derecha
        if ((basProyectil.getX() + basProyectil.getAncho()) >= getWidth()) {
            iVelocidadProyectilX *= -1;
        }
        
        // colision con el techo
        if ((basProyectil.getY() - basProyectil.getAlto()) <= 0) {
            iVelocidadProyectilY *= -1;
        }
        
        // colision con pared izquierda
        if (basProyectil.getX() <= 0) {
            iVelocidadProyectilX *= -1;        
        }
        
        // colision con el suelo
        if (basProyectil.getY() >= getHeight()) {
            // se quita una vida
            iVidas --;
             // se posiciona el proyectil en donde inicio
            basProyectil.setX(basBarra.getX() + basBarra.getAncho() 
                    - (basBarra.getAncho() / 4));
        
            basProyectil.setY(basBarra.getY() - basBarra.getAlto());
            // si se llego al ultimo numero de vidas, se pausa el juego
            if (iVidas > 0) {
                bPausa = !bPausa;
            }
        }
        
        // si la barra llega al borde, se detiene
        if (basBarra.getX() <= 0 ){
               basBarra.setX(0);

        }
        // si la barra llega al borde, se detiene
        else if ((basBarra.getX() + basBarra.getAncho()) >= getWidth()){
            basBarra.setX(getWidth() - basBarra.getAncho());
        }
    }
     /**
     * paint
     *
     * Metodo sobrescrito de la clase <code>JFrame</code>, 
     * 
     * En este metodo lo que hace es actualizar el contenedor y define cuando
     * usar ahora el paint
     *
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     *
     */
     
    public void paint(Graphics graGrafico) {
    // Inicializan el DoubleBuffer
        if (imaImagenFrame == null){
                imaImagenFrame = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaFrame = imaImagenFrame.getGraphics ();
        }
        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("backImage.jpg");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaFrame.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaFrame.setColor (getForeground());
        paint1(graGraficaFrame);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenFrame, 0, 0, this);
       
    }
    
     /**
     * paint1
     * 
     * En este metodo se dibuja las imagenes con la posicion actualizada,
     * ademas que cuando las imagenes no son cargadas te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint1(Graphics graDibujo) {
        
        // el color de la letra es blanco
        graDibujo.setColor(Color.white);
        // se despliegan las vidas
        graDibujo.drawString("Vidas: " + iVidas, 40, 40);
        // se despliegan los puntos
        graDibujo.drawString("Puntos: " + iPuntos, 40, 60);
        // si el juego esta en pausa
        if (bPausa) { 
            // se imprime el aviso de pausado e instrucciones
           graDibujo.drawString("PAUSADO", getWidth() / 2 - 50,  50);
           graDibujo.drawString("*Movimiento con flechas: <- ->", 
                   getWidth() / 8 - 50, getHeight() - (getHeight() / 2));
           graDibujo.drawString("*Pausa y comenzar juego: tecla 'p'", 
                   getWidth() / 8 - 50, getHeight() - (getHeight() / 2) + 20);
           graDibujo.drawString("*Para terminar el juego y cerrar ventana: tecla 'ESC'",
                   getWidth() / 8 - 50, getHeight() - (getHeight() / 2) + 40);
        }
        
        // si el juego termino
        if (bGameOver) {
            // y se acabaron las vidas
            if (iVidas <= 0) {
                // se dibuja la imagen del juego perdido
                graDibujo.drawImage(imgGameOver, 0, 0, this);
                // si los puntos llegan al maximo
            } else if (iPuntos == iCantAnfetaminas) {
                // se dibuja la imagen de finalizacion
                graDibujo.drawImage(imgWellDone, 0, 0, this);
            }
           // el game over se hace falso para poder reiniciar el juego
           bGameOver = !bGameOver;
        } else { // de lo contrario
            // si la imagen ya se cargo
            if (basBarra != null && basPildora != null && basProyectil != null) {
                    //Dibuja la imagen de la barra
                    basBarra.paint(graDibujo, this);
                    // anima el proyectil
                    graDibujo.drawImage(animProyectil.getImagen(), 
                            basProyectil.getX(), basProyectil.getY(), this);
                    // dibuja todas las pildoras
                        for (Base basPildora2 : lklPildoras) {
                                 basPildora2.paint(graDibujo, this);
                        }
           } // sino se ha cargado se dibuja un mensaje 
            else {
                    //Da un mensaje mientras se carga el dibujo	
                    graDibujo.drawString("No se cargo la imagen..", 20, 20);
            
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * KeyPressed
     *
     * Metodo sobrescrito de la clase <code>KeyListener</code>, 
     * En este metodo lo que hace es llevar a cabo acciones cuando
     * una tecla es presionada
     *
     * @param keyEvent es el <code>objeto KeyEvent</code> usado para detectar eventos
     *
     */   
    public void keyPressed(KeyEvent keyEvent) {
      // si se presiona la P mientras se juega
      if (keyEvent.getKeyCode() == KeyEvent.VK_P) {
          if((iPuntos < iCantAnfetaminas) && (iVidas > 0)) {
            //  el juego se pausa
              bPausa = !bPausa;
        }
      }
      // si se presiona escape
      if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
          // el juego termina
          bGameOver = true;
          // y se cierra la ventana 
          this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
      }
      // si se presiona la flecha izquierda
      if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
          iDireccion = 1; // la direccion es izquierda
          bTeclaPresionada = true; // se prende la booleana
        // si se presiona la flecha derecha 
      } else if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            bTeclaPresionada = true; // se prende la booleana
            iDireccion = 2; // se cambia la direccion
        }

      
       // si se presiona entre habiendo finalizado el juego
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            
            if ((iPuntos >= iCantAnfetaminas) || (iVidas <= 0)) {
                lklPildoras.clear(); // se limpia el arraylist
                generarAnfetaminas(); // se generan nuevas anfetaminas
                iVidas = 5; // las vidas vuelven a ser cinco 
                iPuntos = 0; // y la puntuacion regresa a cero

            }
           
        }
      
    }

     /**
     * KeyReleased
     *
     * Metodo sobrescrito de la clase <code>KeyListener</code>, 
     * En este metodo lo que hace es llevar a cabo acciones cuando
     * una tecla es presionada y posteriormente soltada
     *
     * @param keyEvent es el <code>objeto KeyEvent</code> usado para detectar eventos
     *
     */ 
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // cuando cualquiera de las teclas <- o -> se sueltan, ya no se puede mover
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT || 
                keyEvent.getKeyCode() == KeyEvent.VK_RIGHT ) {
            bTeclaPresionada = !bTeclaPresionada;
            iDireccion = 0;
        }
       
        
    } 
        /**
         * main
         * 
         * Metodo main de la clase <code>CuartaTarea.java</code>
         * donde inicia la ejecucion
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Crea un objeto del tipo de la clase para correr el constructor
        BreakingBad t = new BreakingBad();
        // define el tamanio de la ventana
        t.setSize(800, 500);
        // permite que la ventana se pueda cerrar
    	t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // establece la visibilidad del frame como verdadera
    	t.setVisible(true);
    }
        /**
         * generarAnfetaminas()
         * 
         * Metodo que genera objetos de anfetaminas, tres ciclos para cada linea
         * desplegandose de manera intercalada
     * 
     */
    private void generarAnfetaminas() {
        // imagen para las anfetaminas
        URL urlImagenPildora  = this.getClass().getResource("pill.gif");        
       
        // for para la primera fila
        for (int iI = 1; iI < iMAXANCHO*4; iI +=2) {
             basPildora = new Base(100, 100, 
                    Toolkit.getDefaultToolkit().getImage(urlImagenPildora));
            
            basPildora.setY(80); 
            basPildora.setX(basPildora.getX() * iI / 2);
            lklPildoras.add(basPildora);
            
        }
        
        // for para la segunda fila 
        for (int iI = 1; iI < iMAXANCHO ; iI ++) {
            
             basPildora = new Base(100, 100, 
                    Toolkit.getDefaultToolkit().getImage(urlImagenPildora));
            
            basPildora.setY(100); 
            basPildora.setX(basPildora.getX() * iI);
            lklPildoras.add(basPildora);
            
        }
     
        
        // for para la tercera fila
        for (int iI = 1; iI < iMAXANCHO*4; iI +=2) {
             basPildora = new Base(100, 100, 
                    Toolkit.getDefaultToolkit().getImage(urlImagenPildora));
             
            basPildora.setY(120); 
            basPildora.setX(basPildora.getX() * iI / 2);
            lklPildoras.add(basPildora);
            
        }
             
    }
}