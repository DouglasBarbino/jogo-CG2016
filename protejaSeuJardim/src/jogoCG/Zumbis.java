/*
 * Os zumbis sao os viloes do nosso jogo. Eles desejam devorar as plantas para, entao
 * entrarem em nossa casa para nos atacar. Seus atributos sao os seguintes:
 * Vida: eh a quantidade de vida de cada tipo de zumbi. Ao chegar a 0, o zumbi eh eliminado.
 * Velocidade: velocidade da caminhada de um zumbi. Quanto maior a velocidade, mais rapido um zumbi anda.
 * Ataque: eh a quantidade de dano feita por um zumbi em uma planta.
 * Tipo: Temos 3 tipos de zumbis. Cada tipo possui um valor diferente dos atributos anteriores.
 * Os tipos de zumbi podem ser: 1 - Zumbi basico (padrão); 2 - Zumbi cone; 3 - Zumbi balde.
 * Para ver as diferencas de cada zumbi, veja o construtor desta classe. Basicamente, o zumbi básico eh o mais
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
    int x = 803, y = 0; //Da a posicao do zumbi em determinado instante.
    //O zumbi sempre inicia "escondido" no lado extremo DIREITO da tela.
    //A altura aonde o zumbi aparecera na tela eh passada como parametro no construtor.
    // A tela é composta por 3 linhas (fileiras) e 10 colunas
    //200 pixels cada linha (eixo Y) e 80 pixels cada coluna (eixo X)
    
    public void Zumbis(int type, int alturaZ){
    
        this.tipo = type;
        
        //Agora setaremos em qual das 3 fileiras da tela o zumbi aparecerá
        //o zumbi tem altura igual a 90 pixels e cada uma das 3 fileiras tem 600/3 = 200 pixels em Y
        if(alturaZ >= 1 || alturaZ <= 3){            
            switch(alturaZ){ 
                case 1: this.y = 145; break; //1 fileira: 0-200
                case 2: this.y = 245; break; //2 fileira: 200-400
                case 3: this.y = 445; break; //3 fileira: 400-600
            }//fim_switch
        }//fim_if
        else{
            
            this.y = 145;
        }//fim_else
        
        switch(tipo){
        
            case 1: //Zumbi Basico
                this.vida = 8;
                this.velocidade = 1;
                this.ataque = 1;
                break;
                
            case 2: //Zumbi Cone
                this.vida = 16;
                this.velocidade = 1;
                this.ataque = 2;
                break;
                
            case 3: //Zumbi Balde
                this.vida = 30;
                this.velocidade = 2;
                this.ataque = 4;
                break;
            
            default: //zumbi bascio
                this.vida = 8;
                this.velocidade = 1;
                this.ataque = 1;
                break;
        }//fim_switch        
    }//fim_Zumbis()
    
    public void caminhar(){
        
        //Classe para desenhar o zumbi se movimentando
        this.x -= this.velocidade; //Vai do ponto
        try {Thread.sleep(2000); /*O tempo para o zumbi realizar outra caminhada é de 2 segundos*/} 
        catch (InterruptedException e) {System.out.println(e);}
    }//fim_caminhar()
    
    public void atacar(Plantas p){
        
        if(this.x == p.getX() && this.y == p.getY())
            p.vida -= this.ataque;
    }//fim_atacar
    
    public void morrer(){
        
        if(this.vida == 0){
            //apaga desenho do zumbi
        }//fim_if
    }//fim_morrer
    
    void setVelocidade(int i){
        
        velocidade += i; 
    }//fim_setVelocidade
    
    void setX(int i){
        this.x += i;
    }//fim_setX
    
    public int getY(){
        
        return this.y;
    }//fim_getY()
    
}//fim_class_Zumbis
