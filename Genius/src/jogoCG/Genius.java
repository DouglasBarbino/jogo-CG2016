package jogoCG;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
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
    GLU glu = new GLU();
    
    //RGB e alpha (transparência)
    float[] light_ambient = { 0.0f, 0.0f, 0.0f, 1.0f };
    float[] light_diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
    float[] light_specular = { 1.0f, 1.0f, 1.0f, 1.0f };
    float[] light_position = { 2.0f, 5.0f, 5.0f, 0.0f };
    
    //Para o tipo de material
    float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
    
    //Variáveis para manipularem o jogo
    private static Random random = new Random();
    private static int[] ordemJogo = new int[200];//Vetor que armazenara as ordens das cores que o jogador devera clicar
    int[] ordemJogador = new int[200];//vetor que armazenara a ordem de cores clicada pelo jogador.
    private static int countVetor = -1;
    private static int i = 0;

    public static void main(String[] args) {
        count = 0;
        countCirc = 0;
        //countVetor = 0;
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
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        //Definicao do modelo de tonalizacao (ja veio no original)
        gl.glShadeModel(GL.GL_SMOOTH);              
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
       
        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        glu.gluOrtho2D(-width / 2, width / 2, -height / 2, height / 2);//TRATAMENTO (0,0) SER NO CENTRO DA TELA
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); 
        
        //(eyeX, eyeY, eyeZ, aimX, aimY, aimZ, upX, upY, upZ)
        glu.gluLookAt(0.0,0.0,1.0,  0.0,0.0,0.3,  0.0,1.0,0.0);
                
        //definindo todos os componentes da luz 0)
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR,light_specular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION,light_position, 0);
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, light_ambient, 0);
        
        //Define material
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specular, 0);
        //Concentracao do brilho
        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 80);
        
        //So vai executar na primeira iteracao
        if (countVetor < 0){
            desenhaCubos(gl, 4);
            countVetor++;
        }
        else
            gerarOrdem(gl);
        
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    private void desenhaCubos(GL gl, int naoDesenha) {
        
        //Salvando os desenhos para que a translacao nao seja acumulativa
        gl.glPushMatrix();
        gl.glPushMatrix();
        gl.glPushMatrix();
        gl.glPushMatrix();
        
        if (naoDesenha != 0){
            //Cubo verde
            gl.glTranslatef(-0.4f, 0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.0f, 0.2f, 0.0f);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 1){
            //Cubo vermelho
            gl.glTranslatef(0.4f, 0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.2f, 0.0f, 0.0f);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 2){
            //Cubo amarelo
            gl.glTranslatef(-0.4f, -0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.2f, 0.2f, 0.0f);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 3){
            //Cubo azul
            gl.glTranslatef(0.4f, -0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.0f, 0.0f, 0.2f);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 4){
            //Gasta o ultimo popMatrix que estava armazenado
            gl.glPopMatrix();
        }
        gl.glFlush();
    }
    
    //Essa funcao será a responsavel por gerar uma nova cor para o vetor ordemJogo[]
    //public int sorteiaCor(){
        //Sorteara um numero entre 0 e 3
    //    return(random.nextInt(3));
    //}
    
    //Esta funcao servira para inserir e fazer as cores brilharem na ordem que o jogador deve escolher
    public void gerarOrdem(GL gl){
        
        //Salvando os desenhos para que a translacao nao seja acumulativa
        gl.glPushMatrix();
        System.out.println("ordemJogo[]: " +ordemJogo[i]+ " contador: "+i);
            
        switch(ordemJogo[i]){
            case 0: //VERDE 
                //Faco cubo brilhar
                gl.glTranslatef(-0.4f, 0.4f, 0.5f);                    
                gl.glColor3f(0.0f, 1.0f, 0.0f);
                glut.glutSolidCube(0.8f); 
                gl.glPopMatrix();
                break;
                    
            case 1: //VERMELHO
                //Faco cubo brilhar
                gl.glTranslatef(0.4f, 0.4f, 0.5f);                    
                gl.glColor3f(1.0f, 0.0f, 0.0f);
                glut.glutSolidCube(0.8f);
                gl.glPopMatrix();
                break;
                    
            case 2: //AMARELO
                //Faco cubo brilhar
                gl.glTranslatef(-0.4f, -0.4f, 0.5f);                    
                gl.glColor3f(1.0f, 1.0f, 0.0f);
                glut.glutSolidCube(0.8f);
                gl.glPopMatrix();
                break;
                    
            case 3: //AZUL
                //Faco cubo brilhar
                gl.glTranslatef(0.4f, -0.4f, 0.5f);                    
                gl.glColor3f(0.0f, 0.0f, 1.0f);
                glut.glutSolidCube(0.8f);
                gl.glPopMatrix();
                break;
        }  
            
        desenhaCubos(gl, ordemJogo[i]);
        try {Thread.sleep(2000);} 
            catch (InterruptedException e) {System.out.println(e);}
        
        //Um loop for improvisado
        if (i >= countVetor){
            //System.out.println("ordemJogo[]: " +ordemJogo[countVetor]);
            System.out.println("countVetor: " +countVetor);
            countVetor++;
            ordemJogo[countVetor] = random.nextInt(4);
            i = 0;
        }
        else
            i++;
    }
}