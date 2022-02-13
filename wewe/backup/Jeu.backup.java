/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import org.lwjgl.input.Keyboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import env3d.Env;
import env3d.EnvBasic;
/**
 *
 * @author ilyassbouissa
 */
public abstract class Jeu {
    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }
    private final Env env;
    private Room room;
    private Profil profil;
    private ArrayList<Letter> lettres;
    Tux tux;
    Dico dico;
    public Jeu(){
        // Instancie le dico
        dico = new Dico("test");
        
        //On rentre dans le dico les mots du fichier dico.xml
        dico.lireDictionnaireDOM("test/Data/xml/dico.xml","");
        
        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        room = new Room();



        // Instanciez conteneur 
        lettres = new ArrayList<Letter>();

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();
         
    }
    
    //a voir pour la fonction tuxTrouveLettre dans jeuDevinette Mot.java
    public ArrayList<Letter> getLettre(){
        return lettres ; 
    }
    public void execute() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		java.util.Date date = new Date();        
		String dateToStr = dateFormat.format(date);

        // pour l'instant, nous nous contentons d'appeler la méthode joue comme cela
        // et nous créons une partie vide, juste pour que cela fonctionne
        int niveau = (int) ( Math.random()*4+1);
        
        Partie partie = new Partie(dateToStr,dico.getMotDepuisListeNiveaux(niveau),niveau);
        joue(partie);
         
        // Détruit l'environnement et provoque la sortie du programme 
        env.exit();
    }

    public void joue(Partie partie) {

            // TEMPORAIRE : on règle la room de l'environnement. Ceci sera à enlever lorsque vous ajouterez les menus.
            env.setRoom(room);

            // Instancie un Tux
            tux = new Tux(env,room);
            env.addObject(tux);
                        
            // Ici, on peut initialiser des valeurs pour une nouvelle partie
            démarrePartie(partie);

           
            for(char l : partie.getMot().toCharArray()){
                Letter c = new Letter(l,Math.random()*room.getWidth(),Math.random()*room.getHeight());
                lettres.add(c);
                env.addObject(c);
            }

            /* Crée un cube lettre
            Letter c = new Letter('c',Math.random()*room.getWidth(),Math.random()*room.getHeight());
            Letter l = new Letter('l',20,75);
            Letter é = new Letter('é',5,86);

            lettres.add(c);
            lettres.add(l);
            lettres.add(é);

            for (Letter letter : lettres) {
                env.addObject(letter); 
            }
            */

            //On démarre la partie
            démarrePartie(partie);

            // Boucle de jeu
            Boolean finished;
            finished = false;

            int indiceLettre=0;
            while(!finished){
    
                // Contrôles globaux du jeu (sortie, ...)
                //1 is for escape key
                if (env.getKey() == 1) {
                    finished = true;
                }

                // Contrôles des déplacements de Tux (gauche, droite, ...)
                // ... (sera complété plus tard) ...

                // Ici, on applique les regles
                appliqueRegles(partie);
                tux.déplace();
                if(collision(lettres.get(indiceLettre))){
                    System.out.println(distanceC(lettres.get(indiceLettre)));
                    System.out.println(collision(lettres.get(indiceLettre)));

                    lettres.get(indiceLettre).setX((indiceLettre+1)*lettres.get(indiceLettre).getScale()*2);
                    lettres.get(indiceLettre).setZ(2);
                    lettres.get(indiceLettre).setY(15);
                    indiceLettre+=1;
                }
                // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
                env.advanceOneFrame();
            }

            // Ici on peut calculer des valeurs lorsque la partie est terminée
            terminePartie(partie);
        } 

    protected abstract void démarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);

    protected double distanceC(Letter letter){
        
        double Xtux = tux.getX();
        double Ytux = tux.getY();
        double Ztux = tux.getZ();

        double Xlettre = letter.getX();
        double Ylettre = letter.getY();        
        double Zlettre = letter.getZ();
        
        double distance = Math.sqrt( (Xtux-Xlettre)*(Xtux-Xlettre) + (Ytux-Ylettre)*(Ytux-Ylettre) + (Ztux-Zlettre)*(Ztux-Zlettre) ) ;
        double radiusSum = (tux.getScale()/2.0) + (letter.getScale()/2.0);
        //System.out.println( tux.distance(letter));
        return (distance-radiusSum);
    }


    protected boolean collision(Letter letter){
        //Faudra voir si il y a une meilleur methode
        distanceC(letter);
        tux.getScale();
        double radiusSum = (tux.getScale()/2.0) + (letter.getScale()/2.0);
        return ((distanceC(letter)< radiusSum)&&(env.getKeyDown(Keyboard.KEY_E)));
    }

    
}
