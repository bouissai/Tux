import game.Dico;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bouissai
 */
public class TestDico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Dico dico = new Dico("test");
        
        dico.lireDictionnaireDOM("test/Data/xml/dico.xml","");
        for(int i = 1; i<=5; i++){
            System.out.println("Mot de niveau "+(i)+": " + dico.getMotDepuisListeNiveaux(i)+ " " + dico.getMotDepuisListeNiveaux(i));
        }
        
        
        
        
    }
    
}
