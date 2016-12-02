/*
 * Projeteis sao as bolinhas que as plantas soltam para atingir os zumbis e, entao, mata-los.
 * Os projeteis sao de 4 tipos, seguindo o tipo das plantas:
 * 1-Normal; 2-Gelo; 3-Fogo; 4-Pedra 
 */
package jogoCG;


public class Projetil {
    
    int x, y;

    Projetil(int linha, int coluna) {
        this.y = linha; //em que linha, altura a planta esta
        this.x = coluna; //em que coluna a planta esta
    }
    
    public void Lancar(int ataque, Zumbis z){
        
        while(this.x != z.getX()){
                    x += 4;
                }
        //Se saiu do while, atingiu o zumbi
        z.morrer(ataque);        
    }
}
