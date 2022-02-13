/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Attr;
/**
 *
 * @author ilyassbouissa
 */
public class Partie {
    //Nous avons actuellement une classe Partie vide. Nous allons l'améliorer de façon
    // à ce qu'elle soit capable de stocker tout ce qui défini une partie.
    //Les attributs non modifiables 
     private String date ; 
     private String mot ; 
     private int niveau ; 
     private int trouve ; 
     private int temps ; 

    public Partie(String date , String mot , int niveau  ){
        this.date = date ; 
        this.mot = mot ;
        this.niveau = niveau ;
        setTrouve(mot.length()); 
        setTemps(0);
    }

    public Partie(Element partieElt){
        date = partieElt.getAttribute("date");
        temps = Integer.parseInt(partieElt.getChildNodes().item(1).getTextContent());
        mot = partieElt.getChildNodes().item(3).getTextContent();
        niveau = Integer.parseInt( ((Element) partieElt.getChildNodes().item(3)).getAttribute("niveau"));
        setTrouve(mot.length()); 
    }

    public void setDate(String date){
        this.date = date; 
    }

    public void setTrouve( int nbLettresRestantes){
        this.trouve = (int) (( ((double)(mot.length()-nbLettresRestantes))/(double)(mot.length()))*100);
        //System.out.println("VOUS AVEZ TROUVE dans set trouvé" + (  ) +" il reste "+ nbLettresRestantes+ "pour le mot de len"+ mot.length());
    }
    
    public void setTemps (int temps){
        this.temps = temps;
    }

    public int  getNiveau(){
        return niveau ; 
    }
    
    public String getMot(){
        return mot;
    }

    public int getTrouvé() {
        return trouve;
    }

    public int getTemps() {
        return temps;
    }

    public String getDate() {
        return date;
    }

    public Element getPartie(Document doc){
        Element rootElement = doc.createElement("partie");
        doc.appendChild(rootElement);

        Attr attribut_date = doc.createAttribute("date");
        attribut_date.setValue(date);
        rootElement.setAttributeNode(attribut_date);

        // Le temps
        Element temps = doc.createElement("temps");
        temps.appendChild(doc.createTextNode(String.valueOf(temps)));
        rootElement.appendChild(temps);

        // Le mot
        Element mot = doc.createElement("mot");

        Attr attribut_niveau = doc.createAttribute("niveau");
        attribut_niveau.setValue(String.valueOf(niveau));
        rootElement.setAttributeNode(attribut_niveau);

        rootElement.appendChild(mot);

        return (Element) rootElement.getElementsByTagName("Parties");
    }

    public String toString(){
        String s ="- Partie du "+ getDate() +" :\nLe mot de niveau "+getNiveau()+" était "+getMot()+"\nVous avez trouvé "+getTrouvé()+"% du mot en "+getTemps()+" seconde";
        return s;
    }
    }
