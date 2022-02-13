/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.lwjgl.input.Keyboard;
import org.xml.sax.SAXException;

/**
 *
 * @author gladen
 */
public abstract class Jeu {

    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    private final Env env;
    private Tux tux;
    private final Room mainRoom;
    private final Room menuRoom;
    private Letter letter;
    private Profil profil;
    private final Dico dico;
    protected EnvTextMap menuText;                         //text (affichage des texte du jeu)
    private ArrayList<Letter> lettres;
    
    
    public Jeu() {
                // Instanciez conteneur 
                lettres = new ArrayList<Letter>();

        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        mainRoom = new Room("src/game/Data/xml/plateau.xml");

        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règle la camera //mainRoom.getWidth()/2, mainRoom.getHeight()/2,  mainRoom.getDepth()/2
        env.setCameraXYZ(mainRoom.getWidth()/2, mainRoom.getHeight()*0.7,  mainRoom.getDepth()*2.5);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();
        
        // Dictionnaire
        dico = new Dico("src/game/Data/xml/dico.xml");

        // instancie le menuText
        menuText = new EnvTextMap(env);
        
        // Textes affichés à l'écran

        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("2. Sortir de ce jeu ?", "Jeu3", 250, 260);
        menuText.addText("3. Quitter le jeu ?", "Jeu4", 250, 240);
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("3. Sortir du jeu ?", "Principal3", 250, 240);
        menuText.addText("4. Afficher les meilleures scores ?", "Principal4", 250, 220);
        menuText.addText("0. Ajouter un mot ?", "Principal0", 250, 200); // Menu pour ajouter un mot au dico
        menuText.addText("Entrez un niveau", "Niveau", 250, 240);
        menuText.addText("Voulez vous sauvegarder (taper O ou N) ?", "Enregistre", 250, 240);
        menuText.addText("Entrez un nom pour votre partie ?", "filename", 250, 240);
        menuText.addText("Entrez votre date de naissance \"dd/mm/yyyy", "Naissance", 100, 240);
        menuText.addText("Entrez un mot à ajouter ...", "wordNew", 100, 240);
        menuText.addText("Entrez un niveau entre (1-2-3-4-5) ...", "levelNew", 100, 240);
    }

    /**
     * Gère le menu principal
     *
     */
    public void execute() {

        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        this.env.setDisplayStr("Au revoir !", 300, 30);
        env.exit();
    }


    // fourni
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return nomJoueur;
    }
    
    private String wordNew() {
        String motAjoute = "";
        menuText.getText("wordNew").display();
        motAjoute = menuText.getText("wordNew").lire(true);
        menuText.getText("wordNew").clean();
        return motAjoute;
    }


    private String levelNew() {
        String niveauAjoute = "";
        while(!(niveauAjoute.equals("1")||niveauAjoute.equals("2")||niveauAjoute.equals("3")||niveauAjoute.equals("4")||niveauAjoute.equals("5"))){
            menuText.getText("levelNew").display();
            niveauAjoute = menuText.getText("levelNew").lire(true);
            menuText.getText("levelNew").clean();
        }
        return niveauAjoute;
    }

    /*
    // ajouté
    private String getDateNaissance() {
        String DateNaissance = "";
        menuText.getText("Naissance").display();
        DateNaissance = menuText.getText("Naissance").lire(true);
        menuText.getText("Naissance").clean();
        return DateNaissance;
    }*/

    
    // fourni, à compléter
    private MENU_VAL menuJeu() {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);
            
            // affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            //menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();
            
            // vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 ||  touche == Keyboard.KEY_NUMPAD1 || touche == Keyboard.KEY_2 ||  touche == Keyboard.KEY_NUMPAD2 || touche == Keyboard.KEY_3 ||  touche == Keyboard.KEY_NUMPAD3)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            //menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();

            // restaure la room du jeu
            env.setRoom(mainRoom);

            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------        
                case Keyboard.KEY_NUMPAD1 :     
                case Keyboard.KEY_1 : // choisi un niveau et charge un mot depuis le dico
                    // .......... dico.******
                    // restaure la room du menu
                    env.setRoom(menuRoom);
                    menuText.getText("Niveau").display();
            
                    // vérifie qu'une touche 1, 2, 3, 4 ou 5est pressée
                    int niveau = 0;
                    
                    while (!( niveau == Keyboard.KEY_1 || niveau == Keyboard.KEY_2 || niveau == Keyboard.KEY_3 || niveau == Keyboard.KEY_4 || niveau == Keyboard.KEY_5)) {
                        niveau = env.getKey();
                        env.advanceOneFrame();
                    }
                    menuText.getText("Niveau").clean();
                    // restaure la room du menu
                    env.setRoom(mainRoom);

                    dico.lireDictionnaire(); //Je lis le dictionnaire avec SAX
                    // crée un nouvelle partie
                    partie = new Partie("07/09/2018", dico.getMotDepuisListeNiveaux(niveau),niveau);
                    // joue
                    joue(partie);
                    // enregistre la partie dans le profil --> enregistre le profil
                    // .......... profil.******
                    
                    menuText.getText("Enregistre").display();
                    
                    int enregistre = 0;
                    enregistre = env.getKey();
                    while (!(enregistre == Keyboard.KEY_O || enregistre == Keyboard.KEY_N)) {
                        enregistre = env.getKey();
                        env.advanceOneFrame();
                    }

                    if(enregistre == Keyboard.KEY_O){
                        System.out.println(" - J'ajoute ma partie dans mon profil'");
                        profil.ajoutePartie(partie);
                        System.out.println(" - Je la sauvegarde dans "+profil.getNom()+".xml");
                        profil.sauvegarder("src/game/profil/"+profil.getNom()+".xml");
                        
                    }
                    env.advanceOneFrame();
                    menuText.getText("Enregistre").clean();
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;


                // -----------------------------------------
                // Touche 3 : Sortie de ce jeu
                // -----------------------------------------       
                case Keyboard.KEY_NUMPAD2 :              
                case Keyboard.KEY_2:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // -----------------------------------------     
                case Keyboard.KEY_NUMPAD3 :                
                case Keyboard.KEY_3:
                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    private MENU_VAL menuPrincipal() {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;

        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal0").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
        menuText.getText("Principal4").display();
               
        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        
        while (!(touche == Keyboard.KEY_4 ||  touche == Keyboard.KEY_NUMPAD4 || touche == Keyboard.KEY_1 ||  touche == Keyboard.KEY_NUMPAD1 || touche == Keyboard.KEY_2 ||  touche == Keyboard.KEY_NUMPAD2 || touche == Keyboard.KEY_3 ||  touche == Keyboard.KEY_NUMPAD3 || touche == Keyboard.KEY_0 ||  touche == Keyboard.KEY_NUMPAD0)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal0").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();
        menuText.getText("Principal4").clean();

        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {

            // -------------------------------------
            // Touche 0 : Ajoute un mot au dictio 
            // -------------------------------------
            case Keyboard.KEY_NUMPAD0:
            case Keyboard.KEY_0: 
                System.out.println("J'ajoute le mot au dico");
                // Demande le mot a l'utilisateur
                String motAjouter = wordNew();
                //Demande le niveau du mot à l'utulisateur
                int niveauMotajouter = Integer.parseInt(levelNew());
                //Initalise le TestEditeurDico
                TestEditeurDico editeur_dico = new TestEditeurDico();
                //lit le XML du dico et le transforme en DOM
                editeur_dico.lireDOM("src/game/Data/xml/dico.xml");
                //ajoute le mot au DOM
                editeur_dico.ajouterMot(motAjouter, niveauMotajouter);
                //Ecrire l'XML à partir du DOM modifié
                editeur_dico.ecrireDOM("src/game/Data/xml/dico.xml");
                choix = MENU_VAL.MENU_SORTIE;//CONTINUE;
                break;

            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_NUMPAD1:
            case Keyboard.KEY_1:
                // demande le nom du joueur existant
                nomJoueur = getNomJoueur();
                // charge le profil de ce joueur si possible
                if (profil.charge(nomJoueur)) {
                    try {
                        System.out.println("--- Chargement du profil 0% ---");
                        profil = new Profil("src/game/profil/"+nomJoueur+".xml");
                        System.out.println("--- Chargement du profil 100% ---");
                    } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                    choix = menuJeu();
                } else {
                    choix = MENU_VAL.MENU_SORTIE;//CONTINUE;
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_NUMPAD2:
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur
                nomJoueur = getNomJoueur();

                // demande la date de naissance du nouveau joueur
                //String dateNaissance = getDateNaissance();
                // crée un profil avec le nom d'un nouveau joueur
                //demande date de naissance
                //TODO String naissance = getDateNaissance() ;
                profil = new Profil(nomJoueur,"26/10/2000");
                choix = menuJeu();
                break;

            // -------------------------------------
            // Touche 3 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_NUMPAD3:
            case Keyboard.KEY_3:
                choix = MENU_VAL.MENU_SORTIE;
                break;
            // -------------------------------------
            //TODO Touche 4 : Affiche les 10 meilleurs scores
            // -------------------------------------
            case Keyboard.KEY_NUMPAD4:
            case Keyboard.KEY_4:
                    //Gestion des hi-scores
                    Scores scores = new Scores();
                    ArrayList<Integer> highScores = new ArrayList<Integer>();
                    highScores = scores.liste_score("src/game/Data/xml/hi-scores.xml");//Je lis l'xml
                    String meilleurscore = "------TOP 10------\n";
                    Collections.sort(highScores,Collections.reverseOrder());
                    int cmp = 0;
                    for(int k : highScores){
                        cmp++;
                        meilleurscore = meilleurscore + "- Score "+cmp+" - " + k + "\n";
                    }
                        
                    menuText.addText(meilleurscore, "Score",  260, 400);
                    menuText.getText("Score").display();
                    
                    menuText.addText("Entrez quelque chose pour revenir au menu ", "RevenirMenu",  210, 100);
                    String revenir_menu = "";
                    menuText.getText("RevenirMenu").display();
                    revenir_menu = menuText.getText("RevenirMenu").lire(true);
                    menuText.getText("RevenirMenu").clean();                    
                    menuText.getText("Score").clean();
                    //while(true){}
                //choix = MENU_VAL.MENU_JOUE;
        }
        return choix;
    }

    public void joue(Partie partie){

        // restaure la room du menu
        env.setRoom(menuRoom);
        menuText.addText("Le mot est "+partie.getMot(), "LeMot", 250, 240);


        // Affiche le mot pendant 5 seconde
        menuText.getText("LeMot").display();
        try {
            //
            env.getKey();
            env.advanceOneFrame();
            Thread.sleep(4000) ;
            menuText.getText("LeMot").clean();
         }  catch (InterruptedException e) {
             // gestion de l'erreur
         }
        //restaure la room du jeu
        env.setRoom(mainRoom);

        // Instancie un Tux
        tux = new Tux(env, mainRoom);    
        env.addObject(tux);

        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);      
        lettres.clear();
        for(char l : partie.getMot().toCharArray()){
            Letter c = new Letter(l,Math.random()*mainRoom.getWidth(),Math.random()*mainRoom.getDepth());
            lettres.add(c);
            env.addObject(c);
        }
        // Boucle de jeu

        Boolean finished;
        finished = false;
        while (!finished) {
            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1) {
                finished = true;
            }
            // Ici, on applique les regles
            if(!finished)
                finished = appliqueRegles(partie);
            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.déplace();
            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }
        // Ici on peut calculer des valeurs lorsque la partie est terminée
        appliqueRegles(partie);
        terminePartie(partie);
        if(partie.getTrouvé()==100){ //Si on a trouvé le mot on enrengistre le score
            int scores_partie = (partie.getNiveau()*100)/partie.getTemps();
            System.out.println("#SCORE = "+scores_partie);
    
            //Gestion des hi-scores
            Scores scores = new Scores();
            ArrayList<Integer> highScores = new ArrayList<Integer>();
            highScores = scores.liste_score("src/game/Data/xml/hi-scores.xml");//Je lis l'xml
            highScores = scores.HighScore(scores_partie, highScores); 
            scores.sauvegarder_score("src/game/Data/xml/hi-scores.xml", highScores);
            String meilleurscore = "";
            for(int k :highScores){
                meilleurscore = meilleurscore + "- Score - " + k + "\n";
            }
                System.out.println(meilleurscore);
                //menuText.addText("meilleurscore", "Score",  250, 280);
        }
        menuText.getText("E").clean(); 

    }

    public ArrayList<Letter> getLettre(){
        return lettres ; 
    }

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
        //System.out.println(distance-radiusSum);
        return (distance-radiusSum);
    }


    protected boolean collision(Letter letter){
        menuText.addText("Presse \'E\' pour recuperer la lettre ", "E",  getMainRoom().getDepth(),  getMainRoom().getWidth());
        distanceC(letter);
        tux.getScale();
        double radiusSum = (tux.getScale()/2.0) + (letter.getScale()/2.0);
        if((distanceC(letter)<radiusSum))
            menuText.getText("E").display();
        else{
            menuText.getText("E").clean();
        }
        return ((distanceC(letter)<radiusSum)&&(env.getKeyDown(Keyboard.KEY_E)));
    }

    public Room getMainRoom() {
        return mainRoom;
    }
    protected abstract void démarrePartie(Partie partie);

    protected abstract boolean appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);

}
