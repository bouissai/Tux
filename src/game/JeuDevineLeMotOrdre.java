package game ;

public class  JeuDevineLeMotOrdre extends Jeu {

    private int nbLettresRestantes; //permet de stocker le nombre de lettres restantes
    private Chronometre chrono ; //pour gérer le temps de jeu
    private int appelTuxTrouveLettre; //Pour voir combien de fois on entre dans la methode tuxTrouveLettre
    public JeuDevineLeMotOrdre(){
        //Le constructeur de cette classe devra appeler le constructeur de la classe de base Jeu
        chrono = new Chronometre(20); // 20 sec par defaut ?
    }

    protected void démarrePartie(Partie partie){
        // initialisera le compteur de temps notamment,
        // mais également le nombre de lettres restantes 
            appelTuxTrouveLettre = 0;
            chrono.start(); 
            //a faire des qu'on fini la clase partie 
            nbLettresRestantes = partie.getMot().length();
    }

    protected boolean appliqueRegles(Partie partie){
            menuText.addText("- Le chrono est à : "+chrono.getActuel()+"s", "Chrono", getMainRoom().getDepth()*2,  getMainRoom().getHeight()*4);
            menuText.getText("Chrono").display();
            if(chrono.remainsTime()){
                if((tuxTrouveLettre())&&(nbLettresRestantes!=0)){
                    nbLettresRestantes--;
                    System.out.println("Il reste :"+nbLettresRestantes);
                    return false;
                }
                if(nbLettresRestantes==0){
                    System.out.println("Il ne reste plus de lettre\n");
                    //partie.setTemps(chrono.getSeconds());
                    //terminePartie(partie);
                    return true;
                }
                return false;
            }
            else{
                System.out.println("Il ne reste plus de temps");
                //terminePartie(partie);
                return true;
            }
        }

    protected void terminePartie(Partie partie){
        partie.setTrouve(getNbLettresRestantes());
        chrono.stop();
        System.out.println("Stop: " + getTemps() + " seconds.");
        menuText.getText("Chrono").clean();
        partie.setTemps(getTemps());
        System.out.println("Partie terminé ! \n"+partie.toString());


        //return;
    }


    //A completer
    private int getNbLettresRestantes(){
        return nbLettresRestantes ;
    }
    //A completer
    private int getTemps(){
        return chrono.getSeconds(); 
    }

    

    protected boolean tuxTrouveLettre(){
        // A voir avec Ilyass 
        boolean s=false;
        //System.out.println(getNbLettresRestantes());
        if(getNbLettresRestantes()>0){
            s = super.collision(super.getLettre().get(appelTuxTrouveLettre));
            if(s){
                super.getLettre().get(appelTuxTrouveLettre).setX((appelTuxTrouveLettre+1)*super.getLettre().get(appelTuxTrouveLettre).getScale()*2);
                super.getLettre().get(appelTuxTrouveLettre).setZ(2);
                super.getLettre().get(appelTuxTrouveLettre).setY(15);
                appelTuxTrouveLettre++;
            }
        }
        return s;
    }
}