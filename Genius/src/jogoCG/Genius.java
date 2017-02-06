package jogoCG;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_COLOR_MATERIAL;
import static javax.media.opengl.GL.GL_LIGHT0;
import static javax.media.opengl.GL.GL_LIGHTING;
import static javax.media.opengl.GL.GL_NORMALIZE;
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
    private static int count;
    GLUT glut = new GLUT();
    GLU glu = new GLU();       
    
    //Variáveis para manipularem o jogo
    private static Random random = new Random();
    private static int[] ordemJogo = new int[200];//Vetor que armazenara as ordens das cores que o jogador devera clicar
    private static int[] ordemJogador = new int[200];//vetor que armazenara a ordem de cores clicada pelo jogador.
    private static int countVetor = -1;
    private static int countJogador = 0;
    private static int aux = 0;
    private float rtri = 0.0f;
    private static int i = 0;
    //Variavel para autorizar o clique que o jogador der
    private static int liberaClique = 0;
    private static int limpar = 0;
    private static int pontuacao = -1;

    public static void main(String[] args) {
        count = 0;
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
                if (e.getButton() == MouseEvent.BUTTON1 && liberaClique == 1) { //Botao esquerdo
                    x[count] = e.getX() - 392; //centralizando
                    y[count] = e.getY() * -1 + 280; //invertendo e centralizando
                    System.out.println("x,y=" + x[count] + "," + y[count]);//TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                    //Clique no cubo verde
                    if (x[count] < 0 && x[count] > -315 && y[count] >= 0 && y[count] < 225)
                        ordemJogador[countJogador] = 0;
                    if (x[count] >= 0 && x[count] < 315 && y[count] >= 0 && y[count] < 225)
                        ordemJogador[countJogador] = 1;
                    if (x[count] < 0 && x[count] > -315 && y[count] < 0 && y[count] > -225)
                        ordemJogador[countJogador] = 2;
                    if (x[count] >= 0 && x[count] < 315 && y[count] < 0 && y[count] > -225)
                        ordemJogador[countJogador] = 3;
                    count++;
                    aux = countJogador;
                    countJogador++;
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
        
        // Enable VSync
        gl.setSwapInterval(1);
        
        //Definicao do modelo de tonalizacao (ja veio no original)
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glEnable(GL_LIGHT0);
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_NORMALIZE);        
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
        gl.glShadeModel(GL.GL_SMOOTH);
        
        //RGB e alpha (transparência)
        float[] light_ambient = { 0.2f, 0.2f, 0.2f, 1.0f };
        float[] light_diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] light_specular = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] light_position = { 2.0f, 2.0f, -10.0f, 0.0f };
    
        //Para o tipo de material
        float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
        
        //definindo todos os componentes da luz 0)
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR,light_specular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION,light_position, 0);
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, light_ambient, 0);
        
        //Define material
        gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specular, 0);
        //Concentracao do brilho
        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 80);
        
        gl.glLoadIdentity(); 
        
        //(eyeX, eyeY, eyeZ, aimX, aimY, aimZ, upX, upY, upZ)
        //glu.gluLookAt(0.0,0.0,1.0,  0.0,0.0,0.3,  0.0,1.0,0.0);             
           
        if (liberaClique != 1){
            //So vai executar na primeira iteracao
            if (countVetor < 0){
                desenhaCubos(gl, 4);
                countVetor++;
            }
            else                
                gerarOrdem(gl);              
        }
        else{
            //Deixa congelado com os cubos apagados
            //Tempo para o cubo aceso aparecer
            if (limpar == 0){
                try {Thread.sleep(800);} 
                    catch (InterruptedException e) {System.out.println(e);}
            }
            desenhaCubos(gl, 4);
            limpar = 1;
            TextRenderer pontText = new TextRenderer(new Font("Verdana", Font.BOLD, 30));
            //Tamanho da tela
            pontText.beginRendering(800, 600);
            pontText.setColor(0.0f, 0.0f, 0.0f, 1.0f);
            pontText.draw("Turno do JOGADOR", 0, 10);
            pontText.endRendering();
            
            //Ira mostrar as cores clicadas pelo usuario
            gl.glPushMatrix();
            gl.glTranslatef(0.9f, 0.9f, 0.5f);
            gl.glRotatef( rtri, 0.0f, 1.0f, 0.0f );
            
            if(ordemJogador[aux] == 0){
                gl.glColor3f(0.0f, 1.0f, 0.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidSphere(0.1, 20, 10);
            }
            if(ordemJogador[aux] == 1){
                gl.glColor3f(1.0f, 0.0f, 0.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidSphere(0.1, 20, 10);
            }
            if(ordemJogador[aux] == 2){
                gl.glColor3f(1.0f, 1.0f, 0.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidSphere(0.1, 20, 10);
            }
            if(ordemJogador[aux] == 3){
                gl.glColor3f(0.0f, 0.0f, 1.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidSphere(0.1, 20, 10);
            }
            gl.glPopMatrix();
        }
        
        if (countVetor == countJogador ){
            verificaJogada();            
        }        
                
        //Escrita da pontuacao do jogador                
        TextRenderer pontText = new TextRenderer(new Font("Verdana", Font.BOLD, 30));
        //Tamanho da tela
        pontText.beginRendering(800, 600);
        pontText.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        pontText.draw("Pontuacao: " +pontuacao, 0, 570);
        pontText.endRendering();
        
        rtri +=0.2f; 
                
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
        
        //Aqui se verifica qual cubo ja foi desenhado aceso para desenhar os outros,
        //no caso da primeira vez se desenha todos apagados
        if (naoDesenha != 0){
            //Cubo verde
            gl.glTranslatef(-0.4f, 0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.0f, 0.2f, 0.0f);
            gl.glNormal3d(0, 0, 1);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 1){
            //Cubo vermelho
            gl.glTranslatef(0.4f, 0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.2f, 0.0f, 0.0f);
            gl.glNormal3d(0, 0, 1);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 2){
            //Cubo amarelo
            gl.glTranslatef(-0.4f, -0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.2f, 0.2f, 0.0f);
            gl.glNormal3d(0, 0, 1);
            glut.glutSolidCube(0.8f);
            //Retorna o desenho original sem a TRANSLACAO
            gl.glPopMatrix();
        }
        
        if (naoDesenha != 3){
            //Cubo azul
            gl.glTranslatef(0.4f, -0.4f, 0.5f);
            //Draw a simple cube
            gl.glColor3f(0.0f, 0.0f, 0.2f);
            gl.glNormal3d(0, 0, 1);
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
        
        //System.out.println("ordemJogo[]: " +ordemJogo[i]+ " contador: "+i);
        try {Thread.sleep(500);} 
        catch (InterruptedException e) {System.out.println(e);}
        
        //Salvando os desenhos para que a translacao nao seja acumulativa
        gl.glPushMatrix();
            
        switch(ordemJogo[i]){
            case 0: //VERDE 
                //Faco cubo brilhar
                gl.glTranslatef(-0.4f, 0.4f, 0.5f);                    
                gl.glColor3f(0.0f, 1.0f, 0.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidCube(0.8f); 
                gl.glPopMatrix();
                break;
                    
            case 1: //VERMELHO
                //Faco cubo brilhar
                gl.glTranslatef(0.4f, 0.4f, 0.5f);                    
                gl.glColor3f(1.0f, 0.0f, 0.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidCube(0.8f);
                gl.glPopMatrix();
                break;
                    
            case 2: //AMARELO
                //Faco cubo brilhar
                gl.glTranslatef(-0.4f, -0.4f, 0.5f);                    
                gl.glColor3f(1.0f, 1.0f, 0.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidCube(0.8f);
                gl.glPopMatrix();
                break;
                    
            case 3: //AZUL
                //Faco cubo brilhar
                gl.glTranslatef(0.4f, -0.4f, 0.5f);                    
                gl.glColor3f(0.0f, 0.0f, 1.0f);
                gl.glNormal3d(0, 0, 1);
                glut.glutSolidCube(0.8f);
                gl.glPopMatrix();
                break;
        }  
            
        desenhaCubos(gl, ordemJogo[i]);
        
        TextRenderer iText = new TextRenderer(new Font("Verdana", Font.BOLD, 50));
        //Tamanho da tela
        iText.beginRendering(800, 600);
        iText.setColor(0.0f, 0.0f, 0.0f, 1.0f);
        iText.draw(""+(i+1), 0, 300);
        iText.endRendering();
                
        //Um loop for improvisado
        if (i == countVetor){
            //System.out.println("ordemJogo[]: " +ordemJogo[countVetor]);
            System.out.println("countVetor: " +countVetor);
            countVetor++;
            ordemJogo[countVetor] = random.nextInt(4);
            i = 0;
            liberaClique = 1;
            System.out.println("Faca a sequencia!!");
        }
        else{
            i++;
        }            
    }
    
    private void verificaJogada() {    
        for (int indice = 0; indice < countVetor; indice++){
            if (ordemJogo[indice] != ordemJogador[indice]){
                //Volta pro comeco
                indice = countVetor;
                countVetor = -1;
                i = 0;
                pontuacao = -1;
            }
        }
        
        //Jogador nao errou
        if (countVetor != -1){
            pontuacao++;
        }
        
        countJogador = 0;
        liberaClique = 0;
        limpar = 0;
    }
}