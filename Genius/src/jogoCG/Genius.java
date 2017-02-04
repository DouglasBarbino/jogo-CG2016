package jogoCG;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;



/**
 * Genius.java <BR>
 * author: Douglas e Rodolfo
 */
public class Genius implements GLEventListener {

    private static int[] x = new int[1000];
    private static int[] y = new int[1000];
    private static int[] xCirc = new int[1000];
    private static int[] yCirc = new int[1000];
    private static int count;
    private static int countCirc;
    GLUT glut = new GLUT();

    public static void main(String[] args) {
        count = 0;
        countCirc = 0;
        Frame frame = new Frame("Genius");
        final GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new Genius());
        frame.add(canvas);
        frame.setSize(800, 600);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { //Botao esquerdo
                    x[count] = e.getX() - 292; //centralizando
                    y[count] = e.getY() * -1 + 280; //invertendo e centralizando
                    System.out.println("x,y=" + x[count] + "," + y[count]);//TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                    count++;
                } else if (e.getButton() == MouseEvent.BUTTON3) { //Botao esquerdo
                    xCirc[countCirc] = e.getX() - 292; //centralizando
                    yCirc[countCirc] = e.getY() * -1 + 280; //invertendo e centralizando
                    System.out.println("x,y=" + xCirc[countCirc] + "," + yCirc[countCirc]);//TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                    countCirc++;
                }
                canvas.display();
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //Definicao do modelo de tonalizacao (ja veio no original)
        gl.glShadeModel(GL.GL_SMOOTH);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(-width / 2, width / 2, -height / 2, height / 2);//TRATAMENTO (0,0) SER NO CENTRO DA TELA
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        desenhaCubos(gl);
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    private void desenhaCubos(GL gl) {
        //Cubo verde
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        //Draw a simple cube
        gl.glColor3f(0.0f, 0.5f, 0.0f);
        glut.glutSolidCube(10.0f);
        
        //Cubo vermelho
        gl.glTranslatef(2.0f, -2.0f, -6.0f);
        //Draw a simple cube
        gl.glColor3f(0.9f, 0.0f, 0.0f);
        glut.glutSolidCube(1.0f);
        
        //Cubo amarelo
        gl.glTranslatef(-2.0f, 2.0f, 0.0f);
        //Draw a simple cube
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        glut.glutSolidCube(1.0f);
        
        //Cubo azul
        gl.glTranslatef(2.0f, 2.0f, -12.0f);
        //Draw a simple cube
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        glut.glutSolidCube(1.0f);
    }
}