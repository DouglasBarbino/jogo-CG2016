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
    private static Plantas[] planta;
    //private static Projetil[] projetil;
    private static int geraPlanta = 0;
    private static int tipoPlanta = 0;
    private static int contadorPlanta = 0, contZ = 0, contadorZumbi = 0;
    private static int contadorSol = 0;
    private static int geraZumbi = 0;
    private static int xSol = 0;
    private static int ySol = 300;
    private static int sois = 100, zumbisDerrotados = 0, haSois = 0, fimDeJogo = 0;
    //private static int controleSol = 1;
    //Inicializacao da classe randomica
    private static Random random = new Random();
    
    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        final GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new ProtejaSeuJardim());
        frame.add(canvas);
        frame.setSize(800, 600);
        final Animator animator = new Animator(canvas);
        
        System.out.println("SOIS: "+sois);
        
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
                        //TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                        aux1 = count1;
                        count1++;
                        //Para poder gerar uma planta nova, DESDE QUE HAJA SOIS
                        if (geraPlanta == 0)
                            geraPlanta++;
                    }
                    else{
                        //Apenas pega essa coordenada caso for gerar uma planta nova
                        if (geraPlanta == 1 ){
                            if(haSois == 1){
                            xPlanta[count2] = e.getX() - 391; //centralizando
                            yPlanta[count2] = e.getY() * -1 + 280; //invertendo e centralizando
                            //TRATAMENTO DAS COORDENADAS DO MOUSE (0,0) SER NO CENTRO DA TELA E SER POSITIVO PARA CIMA E DIREITA
                            aux2 = count2;
                            count2++;
                            geraPlanta--;
                            haSois--;
                            criaPlanta(tipoPlanta, xPlanta[count2 - 1], yPlanta[count2 - 1]);
                            }else
                                System.out.println("SEM SOIS SUFICIENTES!!!");
                        }
                    }
                    canvas.display();
                }
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
        
        //Criacao dos zumbis
        zumbi = new Zumbis[25];
        //Criacao das plantas
        planta = new Plantas[27];
        
        //random.nextInt(3)+ 1 gera numeros entre 1 e 3
        zumbi[0] = new Zumbis(1, random.nextInt(3)+ 1);
        zumbi[0].setX(-720);
        zumbi[1] = new Zumbis(2, random.nextInt(3)+ 1);
        //zumbi[1].setX(-203);
        zumbi[2] = new Zumbis(3, random.nextInt(3)+ 1);
        //zumbi[2].setX(-123);
        contadorZumbi = 3;
        
        //Sorteia uma coordenada para o solzinho entre -300 e 380
        xSol = random.nextInt(680) - 300;
    }//fim_main()

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
        int mX, mY, mXPlanta, mYPlanta, z, desenhaPlanta, desenhaZumbi, chegouFim;
        
        if (fimDeJogo != 1) {
            for (z = 0; z <= aux1; z++) {
                mX = x[z]; //Coordenadas das Cartas
                mY = y[z];
                mXPlanta = xPlanta[z]; //Coordenadas da planta no gramado
                mYPlanta = yPlanta[z];

                GL gl = drawable.getGL();
                //Aqui que realmente manda desenhar a cor de fundo
                gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();
                desenhaMarcacoes(gl);
                
                //Deteccao da carta que escolheu
                if ((mX >= -400) && (mX <= -320) && (mY <= 300) && (mY >= -300)) {

                    if ((mY <= 300) && (mY > 150) && (sois >= 100)) {//Carta referente a planta 1 - NORMAL
                        tipoPlanta = 1;
                        haSois = 1;
                    }
                    if ((mY <= 150) && (mY > 0) && (sois >= 150)) {//Carta referente a planta 2 - GELO
                        tipoPlanta = 2;
                        haSois = 1;
                    }
                    if ((mY <= 0) && (mY > -150) && (sois >= 200)) {//Carta referente a planta 3 - FOGO
                        tipoPlanta = 3;
                        haSois = 1;
                    }
                    if ((mY <= -150) && (mY >= -300) && (sois >= 250)) {//Carta referente a planta 4 - TERRA
                        tipoPlanta = 4;
                        haSois = 1;
                    }
                }
                /*
            * desenhaLinha(gl, -400, 150, -320, 150);
            * desenhaLinha(gl, -400, 0, -320, 0);
            * desenhaLinha(gl, -400, -150, -320, -150);
                 */

                for (desenhaPlanta = 0; desenhaPlanta < contadorPlanta; desenhaPlanta++) {
                    desenhaPlantas(gl, planta[desenhaPlanta].getX(), planta[desenhaPlanta].getY(), planta[desenhaPlanta].getTipo());
                }

                for (desenhaPlanta = 0; desenhaPlanta < contadorPlanta; desenhaPlanta++) {
                    zumbiMaisProx(planta[desenhaPlanta].getY());
                    //Como o contZ pode nao ter sido atualizado, eh necessario verificar,
                    //caso contrario qualquer projetil vai acertar o primeiro zumbi
                    if (contadorZumbi > 0) {
                        if (planta[desenhaPlanta].getY() == zumbi[contZ].getY()) {
                            planta[desenhaPlanta].atirar(zumbi[contZ]);
                        }
                        if (zumbi[contZ].getMorto() == 1) {
                            zumbi[contZ] = null;
                            ordenaVetorZ(zumbi, contZ);
                            zumbisDerrotados++; //Conta a pontuacao do jogador
                        } else {
                            zumbi[contZ].atacar(planta[desenhaPlanta]);//verifica se zumbi chegou na planta dentro do metodo
                            desenhaProjetil(gl, planta[desenhaPlanta].getTipo(), planta[desenhaPlanta].getProjetil().getX(), planta[desenhaPlanta].getProjetil().getY());
                            if (planta[desenhaPlanta].getMorta() == 1) {
                                planta[desenhaPlanta] = null;
                                ordenaVetorP(planta, desenhaPlanta);
                            }
                        }
                    }
                }

                //Estes sao os desenhos das cartas
                desenhaPlantas(gl, -370, 250, 1);
                desenhaPlantas(gl, -370, 100, 2);
                desenhaPlantas(gl, -370, -50, 3);
                desenhaPlantas(gl, -370, -180, 4);

                for (desenhaZumbi = 0; desenhaZumbi < contadorZumbi; desenhaZumbi++) {
                    desenhaZumbis(gl, zumbi[desenhaZumbi].getX(), zumbi[desenhaZumbi].getY(), zumbi[desenhaZumbi].getTipo());
                    zumbi[desenhaZumbi].caminhar();
                }

                //if (controleSol == 1){ ---> TIREI a variavel controleSol porque ela eh sempre igual a 1
                desenhaSol(gl, xSol, ySol);
                //para mostrar a quantidade de sois no jogo
                desenhaSol(gl, 340, 250);
                //}
                //Atualiza Sol
                contadorSol++;
                ySol -= 25; //O Sol ira parar em y=0
                //Comando para um novo solzinho cair
                if (contadorSol == 12) {
                    contadorSol = 0;
                    sois += 50;//Aumento a quantidade de sois totais no jogo                
                    //Um sol, apos cair, devera ficar no chao ate que o proximo caia. Sempre dois sois ficarao na tela
                    xSol = random.nextInt(680) - 300;
                    ySol = 300;
                    System.out.println("SOIS: " + sois);
                }

                //Atualiza a geracao de zumbi
                geraZumbi++;
                //Comando para um novo zumbi ser gerado
                if (geraZumbi == 48) {//Sera um multiplo do CONTADORSOL!!!
                    //Tipo e posicao aleatorios
                    zumbi[contadorZumbi] = new Zumbis(random.nextInt(3) + 1, random.nextInt(3) + 1);
                    geraZumbi = 0;
                    contadorZumbi++;
                }
                
                //Agora verificaremos se o zumbi chegou na linha final do jogo
                for(chegouFim = 0; chegouFim < contadorZumbi; chegouFim++){
                    System.out.println("X do Zumbi: "+zumbi[chegouFim].getX());
                    if(zumbi[chegouFim].getX() <= -340)
                        fimDeJogo = 1;
                }
                
                //Atualiza o que estah no frame buffer e manda pra tela
                gl.glFlush();
            }//fim_for (z = 0; z <= aux1; z++)
        }
        else{
            GL gl = drawable.getGL();
            //Aqui que realmente manda desenhar a cor de fundo
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
            desenhaMarcacoes(gl);
                
            //Deletaremos as plantas e zumbis
            for(chegouFim = 0; chegouFim < contadorZumbi; chegouFim++){
                    zumbi[chegouFim] = null;
                }
            
            for(chegouFim = 0; chegouFim < contadorPlanta; chegouFim++){
                    planta[chegouFim] = null;
                }
            System.out.println("!!!FIM DE JOGO!!!");
            gl.glFlush();
        }
    }
    
    //Conserta o vetor de zumbis quando algum morre
    public void ordenaVetorZ(Zumbis [] z, int posAtual){
        int nz;
        for(nz = posAtual; nz < (contadorZumbi-1); nz++){
            zumbi[nz] = zumbi[nz+1];
        }
        contadorZumbi--;
    }
    
    //Conserta o vetor de plantas quando alguma morre
    public void ordenaVetorP(Plantas [] p, int posAtual){
        int np;
        for(np = posAtual; np < (contadorPlanta-1); np++){
            planta[np] = planta[np+1];
        }
        contadorPlanta--;
    }
    
    //Determina qual zumbi sera atingido pelos projeteis das plantas
    public void zumbiMaisProx(int yPlant){
        int k, xAux = 400;
        contZ = 0;
        
        for(k = 0; k < contadorZumbi; k++){
            if(yPlant == zumbi[k].getY()){
                if(xAux > zumbi[k].getX()){
                    xAux = zumbi[k].getX();
                    contZ = k;
                }
            }
        }
    }//fim_zumbiMaisProx()

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
    
    //Funcao para criar instancias na classe planta
    private static void criaPlanta(int tipo, int x, int y){
        int localX, localY;
        
        //Para o X basta apenas dividir a coordenada x clicada por 80 e  
        //arredondar para cima, assim vamos conseguir gerar uma posicao
        localX = Math.round(x/80);
        //Correcao necessaria
        if (x > 0){
            localX++;
        }
        //No caso do Y, transladamos 100 pixels para facilitar a conta,
        //além de que a divisao eh feita por 200
        localY = Math.round((y-100)/200);
        //Correcao necessaria
        if (y > 100){
            localY++;
        }
                
        planta[contadorPlanta] = new Plantas(tipo, (localX * 80 - 50), (localY * 200 + 50));
        
        sois -= planta[contadorPlanta].getCusto();
        System.out.println("SOIS: "+sois);
        contadorPlanta++;
    }
    
    private void desenhaProjetil(GL gl, int tipo, int x, int y){
        switch(tipo){
            case 1:
                gl.glColor3f(0.0f, 0.5f, 0.0f);
                gl.glBegin(gl.GL_POINTS);
                desenhaCirculo(gl, x, y, 10);
                gl.glEnd(); break;
            case 2:
                gl.glColor3f(0.5f, 0.5f, 1.0f);
                gl.glBegin(gl.GL_POINTS);
                desenhaCirculo(gl, x, y, 10);
                gl.glEnd(); break;
            case 3:
                gl.glColor3f(0.9f, 0.0f, 0.0f);
                gl.glBegin(gl.GL_POINTS);
                desenhaCirculo(gl, x, y, 10);
                gl.glEnd(); break;
            case 4:
                gl.glColor3f(0.7f, 0.5f, 0.3f);
                gl.glBegin(gl.GL_POINTS);
                desenhaCirculo(gl, x, y, 10);
                gl.glEnd(); break;
        }
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
    private void desenhaZumbis(GL gl, int x, int y, int tipo){
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