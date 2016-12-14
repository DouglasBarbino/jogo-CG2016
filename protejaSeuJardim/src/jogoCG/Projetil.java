/*
 * Projeteis sao as bolinhas que as plantas soltam para atingir os zumbis e, entao, mata-los.
 * Os projeteis sao de 4 tipos, seguindo o tipo das plantas:
 * 1-Normal; 2-Gelo; 3-Fogo; 4-Pedra 
 */
package jogoCG;


public class Projetil {
    
    int x, y, aux;

    Projetil(int linha, int coluna) {
        this.y = linha; //em que linha, altura a planta esta
        this.x = coluna; //em que coluna a planta esta
        this.aux = this.x;
    }
    
    public void Lancar(int ataque, Zumbis z){
        
        if(this.x >= z.getX()){
            System.out.println("PROJETIL = " + this.x+" ZUMBI = "+ z.getX());
            z.morrer(ataque);
            this.x = aux;
        }
        this.x += 10; 
    }
    
    public void setX(int i){
        this.x -= i; 
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
}
