/*
 * Os zumbis sao os viloes do nosso jogo. Eles desejam devorar as plantas para, entao
 * entrarem em nossa casa para nos atacar. Seus atributos sao os seguintes:
 * Vida: eh a quantidade de vida de cada tipo de zumbi. Ao chegar a 0, o zumbi eh eliminado.
 * Velocidade: velocidade da caminhada de um zumbi. Quanto maior a velocidade, mais rapido um zumbi anda.
 * Ataque: eh a quantidade de dano feita por um zumbi em uma planta.
 * Tipo: Temos 3 tipos de zumbis. Cada tipo possui um valor diferente dos atributos anteriores.
 * Os tipos de zumbi podem ser: 1 - Zumbi basico (padrao); 2 - Zumbi cone; 3 - Zumbi balde.
 * Para ver as diferencas de cada zumbi, veja o construtor desta classe. Basicamente, o zumbi basico eh o mais
 * fraco e lento, e possui menor vida. O zumbi balde eh o mais forte, rapido e resistente, e o zumbi cone eh o
 * intermediario.
 */
package jogoCG;

public class Zumbis {
    
    //Atributos
    int vida;
    int velocidade;
    int ataque;
    int tipo;
    int morto; //variavel que diz se um zumbi morreu ou nao
    int x = 403, y = 0; //Da a posicao do zumbi em determinado instante.
    //Lembre-se que o canto superior direito tem coordenadas (400,300) em OpenGL
    //O zumbi sempre inicia "escondido" no lado extremo DIREITO da tela.
    //A altura aonde o zumbi aparecera na tela eh passada como parametro no construtor.
    // A tela eh composta por 3 linhas (fileiras) e 10 colunas
    //200 pixels cada linha (eixo Y) e 80 pixels cada coluna (eixo X)
    
    Zumbis(int type, int alturaZ){
    
        this.tipo = type;
        
        //Agora setaremos em qual das 3 fileiras da tela o zumbi aparecera
        //o zumbi tem altura igual a 90 pixels e cada uma das 3 fileiras tem 600/3 = 200 pixels em Y
        //Lembre-se que o canto superior direito da tela tem coordenadas (400,300) em OpenGL
        if(alturaZ >= 1 || alturaZ <= 3){            
            switch(alturaZ){ 
                case 1: this.y = 250; break; //1 fileira: 300 -> 100 -> 70 pixeis abaixo do canto superior direito
                case 2: this.y = 50; break; //2 fileira: 100-> - 100
                case 3: this.y = -150; break; //3 fileira: -100 -> -300
            }//fim_switch
        }//fim_if
        else{
            
            this.y = 230; //70 pixels abaixo do canto superior direito
        }//fim_else
        
        switch(tipo){
        
            case 1: //Zumbi Basico
                this.vida = 8;
                this.velocidade = 2;
                this.ataque = 1;
                this.morto = 0;
                break;
                
            case 2: //Zumbi Cone
                this.vida = 16;
                this.velocidade = 2;
                this.ataque = 2;
                this.morto = 0;
                break;
                
            case 3: //Zumbi Balde
                this.vida = 24;
                this.velocidade = 3;
                this.ataque = 4;
                this.morto = 0;
                break;
            
            default: //zumbi bascio
                this.vida = 8;
                this.velocidade = 2;
                this.ataque = 1;
                this.morto = 0;
                break;
        }//fim_switch        
    }//fim_Zumbis()
    
    public void caminhar(){
        
        //Classe para desenhar o zumbi se movimentando
        this.x -= this.velocidade; //Vai do ponto
        try {Thread.sleep(300);} 
        catch (InterruptedException e) {System.out.println(e);}
    }//fim_caminhar()
    
    public void atacar(Plantas p){
        int aux = this.velocidade;
        
        if(this.x <= p.getX()+60){
            while(p.getMorta() != 1){
                System.out.println("Ataque Zumbi: " +p.getVida());
                this.velocidade = 0;
                p.morrer(this.ataque);
                try {Thread.sleep(500);} 
                catch (InterruptedException e) {System.out.println(e);}
            }
            this.velocidade = aux;
        }
    }//fim_atacar
    
    public void morrer(int i){
        
        if(this.vida > 0){
            this.vida -= i;
        }else{
            this.morto = 1;
        }
    }//fim_morrer
    
    public void setVelocidade(int i){
        
        this.velocidade += i; 
    }//fim_setVelocidade
    
    public int getVelocidade(){
        return this.velocidade;
    }//fim_getVelocidade()
    
    public void setX(int i){
        
        this.x += i;
    }//fim_setX
    
    public int getX(){
        
        return this.x;
    }//fim_getX()
    
    public int getY(){
        
        return this.y;
    }//fim_getX()
    
    public int getTipo(){
        
        return this.tipo;
    }//fim_getTipo()
    
    public int getMorto(){
        return this.morto;
    }
    
    public int getVida(){
        return this.vida;
    }//fim_getVida();
}//fim_class_Zumbis
