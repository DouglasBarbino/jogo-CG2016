package jogoCG;

import com.sun.opengl.util.Animator;
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
import jogoCG.Plantas;
import jogoCG.Projetil;
import jogoCG.Zumbis;
import java.util.Random;

/**
 * ProtejaSeuJardim.java
 * Autores: Douglas Barbino e Rodolfo Barcelar
 */
public class ProtejaSeuJardim implements GLEventListener {

    private static int[] x = new int[1000];
    private static int[] y = new int[1000];
    private static int[] xPlanta = new int[1000];
    private static int[] yPlanta = new int[1000];
    private static int count1 = 0, count2 = 0;
    private static int aux1 = 0, aux2 = 0;
    private static Zumbis[] zumbi;
    
    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        final GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new ProtejaSeuJardim());
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
        
        //Metodo de deteccao do clique ensinado em aula
        canvas.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //Codigo passado em aula
                if (e.getButton() == MouseEvent.BUTTON1) { //Botao esquerdo
                    //Convertendo o sistema de coordenadas do canvas para o OpenGL
                    //No Windows nao eh tao centralizado assim o clique do mouse
                    if ((e.getX() - 391) <= -320) {
                        x[count1] = e.getX() - 391; //centralizando
                        y[count1] = e.getY() * -1 + 280; //invertendo e centralizando
                        System.out.println("x,y=" + x[count1] + "," + y[count1]);//TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                        aux1 = count1;
                        count1++;
                    }
                    else{
                        xPlanta[count2] = e.getX() - 391; //centralizando
                        yPlanta[count2] = e.getY() * -1 + 280; //invertendo e centralizando
                        System.out.println("xPlanta,yPlanta=" + xPlanta[count2] + "," + yPlanta[count2]);//TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                        aux2 = count2;
                        count2++;
                    }
                    canvas.display();
                }
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
        
        //Inicializacao da classe randomica
        Random random = new Random();
        
        //Criacao dos zumbis
        zumbi = new Zumbis[25];
        /*for(int i = 0;i<zumbi.length;i++)  
            zumbi[i] = new Zumbis(1, );*/
        //random.nextInt(3)+ 1 gera numeros entre 1 e 3
        zumbi[0] = new Zumbis(1, random.nextInt(3)+ 1);
        zumbi[0].setX(-283);
        zumbi[1] = new Zumbis(2, random.nextInt(3)+ 1);
        zumbi[1].setX(-203);
        zumbi[2] = new Zumbis(3, random.nextInt(3)+ 1);
        zumbi[2].setX(-123);
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        //AQUI SOH DEFINE A COR DE FUNDO, AINDA NAO DESENHOU
        gl.glClearColor(0.0f, 0.75f, 0.0f, 0.0f);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //Tudo isso eh padrao por enquanto
        GL gl = drawable.getGL();
        GLU glu = new GLU();
        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        gl.glViewport(0, 0, width, height);
        //Carregar a matriz para 3D
        gl.glMatrixMode(GL.GL_PROJECTION);
        //Carregou a matriz, carrega a identidade dela
        gl.glLoadIdentity();
        //Area de visao
        glu.gluOrtho2D(-width / 2, width / 2, -height / 2, height / 2);//TRATAMENTO (0,0) SER NO CENTRO DA TELA
        //Onde ficam os desenhos
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        int mX, mY, mXPlanta, mYPlanta, z;
        for (z = 0; z <= aux1; z++) {
            mX = x[z];
            mY = y[z];
            mXPlanta = xPlanta[z];
            mYPlanta = yPlanta[z];

            GL gl = drawable.getGL();
            //Aqui que realmente manda desenhar a cor de fundo
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
            desenhaMarcacoes(gl);
            //Deteccao da carta que escolheu
            if ((mX >= -400) && (mX <= -320) && (mY <= 300) && (mY >= -300) /*&& (mX != 0) && (mY != 0)*/) {

                if ((mY <= 300) && (mY > 150)) {//Carta referente a planta 1 - NORMAL
                    //Plantas(1, 230/70/-170, -350/-270/-190/-110/-30/50/130/210/290/370 );
                    desenhaPlantas(gl, -290, 250, 1);
                    //yPlanta[aux2] = 0;
                }
                if ((mY <= 150) && (mY > 0)) {//Carta referente a planta 2 - GELO
                    //Plantas(2, 230/70/-170, -350/-270/-190/-110/-30/50/130/210/290/370 );
                    desenhaPlantas(gl, -290, 50, 2);
                    //yPlanta[aux2] = 0;
                }
                if ((mY <= 0) && (mY > -150)) {//Carta referente a planta 3 - FOGO
                    //Plantas(3, 230/70/-170, -350/-270/-190/-110/-30/50/130/210/290/370 );                
                    desenhaPlantas(gl, -210, -150, 3);
                    //yPlanta[aux2] = 0;
                }
                if ((mY <= -150) && (mY >= -300)) {//Carta referente a planta 4 - TERRA
                    //Plantas(4, 230/70/-170, -350/-270/-190/-110/-30/50/130/210/290/370 );                
                    desenhaPlantas(gl, -290, 50, 4);
                    //yPlanta[aux2] = 0;
                }
            }
            /*
            * desenhaLinha(gl, -400, 150, -320, 150);
            * desenhaLinha(gl, -400, 0, -320, 0);
            * desenhaLinha(gl, -400, -150, -320, -150);
            */

            desenhaPlantas(gl, -370, 250, 1);
            desenhaPlantas(gl, -370, 100, 2);
            desenhaPlantas(gl, -370, -50, 3);
            desenhaPlantas(gl, -370, -180, 4);
            desenhaSol(gl, 0, 0);
            desenhaZumbi(gl, zumbi[0].getX(), zumbi[0].getY(), zumbi[0].getTipo());
            desenhaZumbi(gl, zumbi[1].getX(), zumbi[1].getY(), zumbi[1].getTipo());
            desenhaZumbi(gl, zumbi[2].getX(), zumbi[2].getY(), zumbi[2].getTipo());
            //Atualiza o que estah no frame buffer e manda pra tela
            gl.glFlush();
        }
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    //x e y sao as coordenadas do ponto que liga a cabeca ao resto do corpo da planta
    //tipo serve para definir o poder da planta, sendo:
    //1=normal, 2=gelo, 3=fogo, 4=terra
    private void desenhaPlantas(GL gl, int x, int y, int tipo){
        switch (tipo) {
            case 1:
                gl.glColor3f(0.0f, 0.5f, 0.0f);
                break;
            case 2:
                gl.glColor3f(0.5f, 0.5f, 1.0f);
                break;
            case 3:
                gl.glColor3f(0.9f, 0.0f, 0.0f);
                break;
            case 4:
                gl.glColor3f(0.7f, 0.5f, 0.3f);
                break;
            default:
                gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glBegin(gl.GL_POINTS);
        //Triangulo da cabeca
        desenhaLinha(gl, x, y, x+50, y-25);
        desenhaLinha(gl, x+50, y-25, x+50, y+25);
        desenhaLinha(gl, x+50, y+25, x, y);
        //Corpo e pes
        desenhaLinha(gl, x, y, x, y-50);
        desenhaLinha(gl, x, y-50, x-25, y-100);
        desenhaLinha(gl, x, y-50, x+25, y-100);
        gl.glEnd();
    }
    
    //x e y eh o ponto central
    private void desenhaSol(GL gl, int x, int y){
        gl.glColor3f(0.95f, 0.95f, 0.0f);
        gl.glBegin(gl.GL_POINTS);
        desenhaCirculo(gl, x, y, 10);
        gl.glEnd();
    }
    
    //x e y sao as coordenadas do ponto que liga a cabeca ao resto do corpo do zumbi
    //tipo serve para definir o poder da zumbi, sendo:
    //1=normal, 2=cone, 3=balde
    private void desenhaZumbi(GL gl, int x, int y, int tipo){
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glBegin(gl.GL_POINTS);
        //Cabeca e corpo
        desenhaCirculo(gl, x, y+15, 15);
        desenhaLinha(gl, x, y, x, y-50);
        //Pernas e bracos
        desenhaLinha(gl, x, y-50, x+10, y-100);
        desenhaLinha(gl, x, y-50, x-20, y-100);
        desenhaLinha(gl, x, y-10, x-30, y-30);
        desenhaLinha(gl, x, y-10, x-25, y+15);
        switch (tipo) {
            case 2:
                //Desenho do cone
                gl.glColor3f(0.9f, 0.5f, 0.0f);
                desenhaLinha(gl, x+25, y+15, x-25, y+15);
                desenhaLinha(gl, x+20, y+15, x, y+45);
                desenhaLinha(gl, x, y+45, x-20, y+15);
                break;
            case 3:
                //Desenho do balde
                gl.glColor3f(0.7f, 0.7f, 0.7f);
                desenhaLinha(gl, x+20, y+15, x-20, y+15);
                desenhaLinha(gl, x-20, y+15, x-10, y+35);
                desenhaLinha(gl, x-10, y+35, x+10, y+35);
                desenhaLinha(gl, x+10, y+35, x+20, y+15);
                break;
            default:
                gl.glColor3f(0.0f, 0.0f, 0.0f);
        }
        gl.glEnd();
    }
    
    /**
     * desenhaLinha (Bresenham Algorithm)
     * Utilizado conforme foi passado em aula
     * x1 e y1 sao um ponto, x2 e y2 sao outro ponto
     */
    private void desenhaLinha(GL gl, int x1, int y1, int x2, int y2) {
        int slope;
        boolean inverte = false;
        int dx, dy, incE, incNE, d, x, y;

        //Garantir x1<x2 sempre, inverter pontos se necessario
        if (x1 > x2) {
            desenhaLinha(gl, x2, y2, x1, y1);
            return;
        }
        dx = x2 - x1;
        dy = y2 - y1;
        int t;

        //Se dy>dx temos octantes pares e ai temos que trocar x,y nos calculos
        if (Math.abs(dy) > dx) {

            inverte = true; //flag que sinaliza a inversao de x e y
            t = x1;
            x1 = y1;
            y1 = t;

            t = x2;
            x2 = y2;
            y2 = t;

            t = dx;
            dx = dy;
            dy = t;

            //se x e y foi trocado, novamente temos que garantir que xi < x2
            if (x2 < x1) {
                t = x1;
                x1 = x2;
                x2 = t;

                t = y1;
                y1 = y2;
                y2 = t;

                dx = x2 - x1;
                dy = y2 - y1;
            }

        }

        //se a varicao dy eh negativa, entao ao inves de incrementar temos que decrementar o y
        if (dy < 0) {
            slope = -1;
            dy = -dy; // aqui garantimos sempre dy positiva, so no desenho decrementamos ao inves de incrementar
        } else {
            slope = 1;
        }
        incE = 2 * dy;
        incNE = 2 * dy - 2 * dx;
        d = 2 * dy - dx;
        y = y1;
        for (x = x1; x <= x2; x++) {
            if (inverte) { // se inverteu, entao desenha invertido o ponto real
                gl.glVertex2i(y, x);
            } else {
                gl.glVertex2i(x, y);
            }
            if (d <= 0) {
                d += incE;
            } else {
                d += incNE;
                y += slope;
            }
        }
    }
    
    /**
     * desenhaCirculo (Bresenham Algorithm)
     * Utilizado conforme foi passado em aula
     * xc e yc eh o ponto central, raio eh o raio!
     */
    private void desenhaCirculo(GL gl, int xc, int yc, int raio) {
        int d, x, y;
        
        d = 1 - raio;
        x = 0;
        y = raio;
        pontosCirculo(x, y, xc, yc, gl);
        
        while(x<y){
            if (d <= 0){
                d = d + 2 * x + 3;
                x++;
            }
            else{
                d = d + 2 * x - 2 * y + 5;
                x++;
                y--;
            }
            pontosCirculo(x, y, xc, yc, gl);
        }
    }
    
    public void pontosCirculo(int x, int y, int xc, int yc, GL gl){
        gl.glVertex2i(y+xc, x+yc);
        gl.glVertex2i(y+xc, -x+yc);
        gl.glVertex2i(-y+xc, -x+yc);
        gl.glVertex2i(-y+xc, x+yc);
        gl.glVertex2i(x+xc, y+yc);
        gl.glVertex2i(x+xc, -y+yc);
        gl.glVertex2i(-x+xc, -y+yc);
        gl.glVertex2i(-x+xc, y+yc);
    }
    
    public void desenhaMarcacoes(GL gl){
        gl.glColor3f(0.0f, 0.9f, 0.0f);
        gl.glBegin(gl.GL_POINTS);
        //Linhas horizontais
        desenhaLinha(gl, -320, -100, 400, -100);
        desenhaLinha(gl, -320, 100, 400, 100);
        //Linhas verticais
        desenhaLinha(gl, -240, -300, -240, 300);
        desenhaLinha(gl, -160, -300, -160, 300);
        desenhaLinha(gl, -80, -300, -80, 300);
        desenhaLinha(gl, 0, -300, 0, 300);
        desenhaLinha(gl, 80, -300, 80, 300);
        desenhaLinha(gl, 160, -300, 160, 300);
        desenhaLinha(gl, 240, -300, 240, 300);
        desenhaLinha(gl, 320, -300, 320, 300);
        
        //Cartas, linha vertical
        //Cor preta para as marcações        
        gl.glColor3f(0.0f, 0.5f, 0.0f);        
        desenhaLinha(gl, -320, -300, -320, 300);
        //CARTAS, horizontal
        desenhaLinha(gl, -400, 150, -320, 150);
        desenhaLinha(gl, -400, 0, -320, 0);
        desenhaLinha(gl, -400, -150, -320, -150);
        
        gl.glEnd();
    }
}