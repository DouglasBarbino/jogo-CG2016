/*
 * As plantas sao as heroinas do nosso jogo. Elas protegem nossa casa dos zumbis e atiram sementes para derrotarem
 * os zumbis antes que eles entrem em nossa casa
 * Vida: eh a quantidade de vida de cada tipo de planta. Ao chegar a 0, planta morre.
 * Ataque: eh a quantidade de dano feita por uma planta em um zumbi.
 * Custo: é a quantidade de sóis para se plantar uma planta.
 * Tipo: Temos 4 tipos de plantas. Cada tipo possui um valor diferente dos atributos anteriores ou algum efeito 
 * sobre os zumbis.
 * Os tipos de plantas podem ser: 1 - Planta Normal (padrão); 2 - Planta de Gelo; 3 - Planta de Fogo; 4 - Planta de Terra.
 * As plantas normais lancam projeteis normais, nao causam efeitos e tem menor vida e ataque;
 * As plantas de gelo lancam projeteis congelantes que retardam zumbis da mesma fileira. Tem menor vida e ataque;
 * As plantas de fogo lancam projeteis de fogo e nao tem efeitos, mas tem maior ataque e vida;
 * As plantas de terra lancam projeteis de pedra que afastam os zumbis, tem maior vida e ataque.
 */
package jogoCG;

public class Plantas {
    
    int vida;
    int ataque;
    int custo;
    int tipo;
    int congelar = 0;
    int afastar = 0;
    int x, y; //x e y são as coordenadas da planta
    
    public void Plantas(int type, int colunaP, int linhaP){
        
        this.x = colunaP;
        this.y = linhaP;
        
        if(type >= 1 || type <= 4)
            this.tipo = type;
        else
            this.tipo = 1;
        
        switch(tipo){
            case 1: //planta normal
                this.vida = 4;
                this.ataque = 1;
                this.custo = 100;
                break;
                
            case 2: //planta de gelo
                this.vida = 4;
                this.ataque = 1;
                this.custo = 150;
                this.congelar = 1;
                break;
                
            case 3: //planta de fogo
                this.vida = 8;
                this.ataque = 3;
                this.custo = 200;
                break;
                
            case 4: //planta de terra
                this.vida = 8;
                this.ataque = 4;
                this.custo = 225;
                this.afastar = 1;
                break;
                
            default: //planta normal
                this.vida = 4;
                this.ataque = 1;
                this.custo = 100;
                break;
        }//fim_switch
    }//fim_Plantas()
    
    public void atacar(Zumbis z){
        
        if(z.getY() == this.y){
            //lança projetil
            if(this.congelar == 1)
                z.setVelocidade(-1);
            if(this.afastar == 1)
                z.setX(-1); 
        }//fim_if
        
    }//fim_atacar()
    
    public void morrer(){
        
        //apaga desenho da planta
    }//fim_morrer()
    
    public int getX(){
        
        return this.x;
    }//fim_getX
    
    public int getY(){
        
        return this.y;
    }//fim getY
}//fim_class_Plantas
