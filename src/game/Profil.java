/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author ilyassbouissa
 */
public class Profil {
    private String nom;
    private String dateNaissance;
    private String avatar;
    private ArrayList<Partie> parties;

    public Document _doc;
    
    public Profil(){
        parties = new ArrayList<Partie>();
        this.nom = "ilyass";
        this.dateNaissance = "26-10-2000";
    }


    public Profil(String nomJoueur, String dateNaissance ){
        parties = new ArrayList<Partie>();
        this.nom = nomJoueur;
        this.dateNaissance = dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<Partie> getParties() {
        return parties;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }
    //instancie un profil à partir d'un fichier xml
    public Profil(String filename) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException{
        parties = new ArrayList<Partie>();
        _doc = fromXML(filename);
        //On recupère la racine du doc xml profil.xml
        nom = _doc.getElementsByTagName("nom").item(0).getTextContent();
        System.out.println("J'ai recuperèrer le nom :"+nom);
        avatar = _doc.getElementsByTagName("avatar").item(0).getTextContent();
        System.out.println("J'ai recuperèrer l'avatar :"+avatar);
        dateNaissance = xmlDateToProfileDate(_doc.getElementsByTagName("anniversaire").item(0).getTextContent()); //xmlDateToProfileDate()Transforme la date XML en format yyy/mm/dd
        System.out.println("J'ai recuperèrer l'anniversaire : "+dateNaissance);

        Element racine = _doc.getDocumentElement();

        //On recupère les parties
        NodeList parties_xml = racine.getElementsByTagName("parties");
        System.out.println("On recupère les parties : ");
        // Pour chaque partie je l'ajoute à l'arrayList en attributs
        for(int i=0;i<parties_xml.getLength();i++){
            //System.out.println("Partie : "+parties_xml.item(i).getTextContent());
            Element partie_xml =  (Element) parties_xml.item(i);
            System.out.println("Partie : "+partie_xml.getAttribute("date"));
            Partie partie = new Partie((Element) partie_xml.getElementsByTagName("partie").item(i));
            partie.setDate(xmlDateToProfileDate(partie.getDate()));
            ajoutePartie(partie);
        }
    }

    // Cree un DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //rajouter à la liste des parties une Partie instanciée.
    public void ajoutePartie(Partie p){
        //System.out.println(p.toString()+" est ajouté...");
        parties.add(p);
    }

    // Sauvegarde un DOM en XML
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // sauvegarder le document DOM dans un fichier XML.
    void sauvegarder(String filename){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            _doc = dBuilder.newDocument();

            //Element racine <profil>
            Element racineProfil = _doc.createElement("profil");
            _doc.appendChild(racineProfil);
            
            //Element fils <nom>
            Element profilNom = _doc.createElement("nom");
            profilNom.appendChild(_doc.createTextNode(nom));
            racineProfil.appendChild(profilNom);
    
            //Element fils <avatar>
            Element profilAvatar= _doc.createElement("avatar");
            profilAvatar.appendChild(_doc.createTextNode(avatar));
            racineProfil.appendChild(profilAvatar);
    
            //Element fils <anniversaire>
            Element profilAnniversaire= _doc.createElement("anniversaire");
            profilAnniversaire.appendChild(_doc.createTextNode(profileDateToXmlDate(dateNaissance)));
            racineProfil.appendChild(profilAnniversaire);
    
            //Element fils <parties>
            Element profilParties = _doc.createElement("parties");
            racineProfil.appendChild(profilParties);
                //Element fils <partie>
                for(int i=0;i<parties.size();i++){
                    //<partie date="2020-02-24"
                    Element profilPartie= _doc.createElement("partie");
                    profilPartie.setAttribute("date", profileDateToXmlDate(parties.get(i).getDate())); ///on transformùe la date en date format xml
                    profilPartie.setAttribute("trouvé", String.valueOf(parties.get(i).getTrouvé())+"%");
                        //<temps> 20.0 <temps>
                        Element partieTemps = _doc.createElement("temps");
                        partieTemps.appendChild(_doc.createTextNode(String.valueOf(parties.get(i).getTemps())));
                        profilPartie.appendChild(partieTemps);
                        //<mot niveau="1"> abcd <mot>
                        Element partieMot = _doc.createElement("mot");
                        partieMot.setAttribute("niveau", String.valueOf(parties.get(i).getNiveau()));
                        partieMot.appendChild(_doc.createTextNode(parties.get(i).getMot()));
                        profilPartie.appendChild(partieMot);
                    profilParties.appendChild(profilPartie);
                }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        toXML(filename);
    }


    public boolean charge(String nomJoueur){
        File fichier;
 
        fichier = new File("src/game/profil/"+nomJoueur+".xml");
 
        if (fichier.exists()&& (!fichier.isDirectory())) {
            System.out.println("Le fichier /src/game/profil/"+nomJoueur+".xml existe");
            return true;
        } else {
            System.out.println("Le fichier /src/game/profil/"+nomJoueur+".xml n'existe pas");
            return false;
        }
        //return this.nom.equals(nomJoueur);
    }

    
    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        //System.out.println("xmlDate à changer : "+xmlDate);
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        //System.out.println("profileDate à changer : "+profileDate);
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));
        return date;
    }
}
