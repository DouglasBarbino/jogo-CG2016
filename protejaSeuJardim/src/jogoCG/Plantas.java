/*
 * As plantas sao as heroinas do nosso jogo. Elas protegem nossa casa dos zumbis e atiram sementes para derrotarem
 * os zumbis antes que eles entrem em nossa casa
 * Vida: eh a quantidade de vida de cada tipo de planta. Ao chegar a 0, planta morre.
 * Ataque: eh a quantidade de dano feita por uma planta em um zumbi.
 * Custo: eh a quantidade de sois para se plantar uma planta.
 * Tipo: Temos 4 tipos de plantas. Cada tipo possui um valor diferente dos atributos anteriores ou algum efeito 
 * sobre os zumbis.
 * Os tipos de plantas podem ser: 1 - Planta Normal (padrao); 2 - Planta de Gelo; 3 - Planta de Fogo; 4 - Planta de Terra.
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
    int congelar;
    int afastar;
    int morta;
    int x, y; //x e y são as coordenadas da planta
    Projetil projetil;
    
    Plantas(int type, int colunaP, int linhaP){
        
        this.x = colunaP;
        this.y = linhaP;
        this.projetil = new Projetil(this.y, this.x);
        
        if(type >= 1 || type <= 4)
            this.tipo = type;
        else
            this.tipo = 1;
        
        switch(tipo){
            case 1: //planta normal
                this.vida = 8;
                this.ataque = 1;
                this.custo = 100;
                this.congelar = 0;
                this.afastar = 0;
                this.morta = 0;
                break;
                
            case 2: //planta de gelo
                this.vida = 8;
                this.ataque = 1;
                this.custo = 150;
                this.congelar = 1;
                this.afastar = 0;
                this.morta = 0;
                break;
                
            case 3: //planta de fogo
                this.vida = 16;
                this.ataque = 2;
                this.custo = 200;
                this.congelar = 0;
                this.afastar = 0;
                this.morta = 0;
                break;
                
            case 4: //planta de terra
                this.vida = 16;
                this.ataque = 2;
                this.custo = 250;
                this.congelar = 0;
                this.afastar = 1;
                this.morta = 0;
                break;
                
            default: //planta normal
                this.vida = 8;
                this.ataque = 1;
                this.custo = 100;
                this.congelar = 0;
                this.afastar = 0;
                this.morta = 0;
                break;
        }//fim_switch
    }//fim_Plantas()
    
    public void atirar(Zumbis z){
        this.projetil.Lancar(this.ataque, z);
        if((this.congelar == 1) && (z.getVelocidade() > 1) && (z.getX() <= this.projetil.getX())) {
            z.setVelocidade(-1);
        }
        if((this.afastar == 1) && (z.getX() < 400) && (z.getX() <= this.projetil.getX())) {
            z.setX(10);
        }
    }//fim_atirar()
    
    public void morrer(int i){
        
        if(this.vida == 0)
            this.morta = 1;
        else
            this.vida -= i;
    }//fim_morrer()
    
    public int getX(){
        
        return this.x;
    }//fim_getX
    
    public int getY(){
        
        return this.y;
    }

    public int getTipo(){
        
        return this.tipo;
    }//fim getY
    
     public int getAtaque(){
        
        return this.ataque;
    }//fim getAtaque
     
    public Projetil getProjetil(){
        return this.projetil;
    }//fim_getProjetil()
    
    public int getMorta(){
        return this.morta;
    }//fim_getMorta()
    
    public int getVida(){
        return this.vida;
    }//fim_getVida()
    
    public int getCusto(){
        return this.custo;
    }
}//fim_class_Plantas
