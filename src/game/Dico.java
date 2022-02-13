/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.xpath.XPath;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author bouissai
 */
public class Dico extends DefaultHandler {
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    private String cheminFichierDico;
    StringBuffer buffer;
    
    public Dico(String cheminFichierDico){
        super();
        this.cheminFichierDico = cheminFichierDico;
        listeNiveau1 = new ArrayList<>();
        listeNiveau2 = new ArrayList<>();
        listeNiveau3 = new ArrayList<>();
        listeNiveau4 = new ArrayList<>();
        listeNiveau5 = new ArrayList<>();
    }

    // return la liste lie au niveau
    public void lireDictionnaire(){
        try{ 
			// création d'une fabrique de parseurs SAX 
			SAXParserFactory fabrique = SAXParserFactory.newInstance(); 
  
			// création d'un parseur SAX 
			SAXParser parseur = fabrique.newSAXParser(); 
  
			// lecture d'un fichier XML avec un DefaultHandler 
			File fichier = new File(cheminFichierDico); 
			DefaultHandler gestionnaire = new DefaultHandler(){
                String niveau;
                // Actions à réaliser lors de la détection d'un nouvel élément.
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    //Je recupère l'attribut "niveau" du mot
                    if(qName.equals("mot")){
                        niveau = attributes.getValue(0); 
                    }
                    else{
                        buffer = new StringBuffer(); 
                    }
                    buffer.setLength(0); //On clear le buffer
                }
                //Actions à réaliser lors de la détection de la fin d'un élément.
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if(qName.equals("mot")){
                        //Selon le niveau que j'ai obtenu dans startElement j'ajoute le mot à la liste correspondante
                        switch(Integer.parseInt(niveau)){ 
                            case 1 : listeNiveau1.add(buffer.toString()); break;
                            case 2 : listeNiveau2.add(buffer.toString()); break;
                            case 3 : listeNiveau3.add(buffer.toString()); break;
                            case 4 : listeNiveau4.add(buffer.toString()); break;
                            case 5 : listeNiveau5.add(buffer.toString()); break;
                            default:
                        }
                    }
                }
                //Actions à réaliser sur les données
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    //Mon buffer si il existe recupère la donnée de la balise mot
                    if(buffer != null){
                        buffer.append(ch,start,length); 
                    }
                }

                @Override
                public void startDocument() throws SAXException {}

                @Override
                public void endDocument() throws SAXException {} 

            }; 
			parseur.parse(fichier, gestionnaire); 
  
		}catch(ParserConfigurationException pce){ 
			System.out.println("Erreur de configuration du parseur"); 
			System.out.println("Lors de l'appel à newSAXParser()"); 
		}catch(SAXException se){ 
			System.out.println("Erreur de parsing"); 
			System.out.println("Lors de l'appel à parse()"); 
		}catch(IOException ioe){ 
			System.out.println("Erreur d'entrée/sortie"); 
			System.out.println("Lors de l'appel à parse()"); 
		} 
    }
  
    
    public String getMotDepuisListeNiveaux(int niveau){
       /* String mot ="";
        ArrayList<String> liste = new ArrayList<String>();
        //int verifie = 
        liste.add("listeNiveau"+(vérifieNiveau(niveau)));
        mot = getMotDepuisListe(liste);*/
       String mot = "";
       switch(vérifieNiveau(niveau)){
           case 1 : 
               mot = getMotDepuisListe(listeNiveau1);
               break;
           case 2 : 
              mot = getMotDepuisListe(listeNiveau2);
              break;
           case 3 : 
               mot = getMotDepuisListe(listeNiveau3);
               break;
           case 4 : 
               mot = getMotDepuisListe(listeNiveau4);
               break;
            case 5 : 
               mot = getMotDepuisListe(listeNiveau5);
               break;
               
       }
       return mot;
    }

    public void ajouteMotADico(int niveau, String mot){
        switch(vérifieNiveau(niveau)){
            case 1:
                listeNiveau1.add(mot);
                break ;
            case 2:
                listeNiveau2.add(mot); 
                break ;
            case 3:
                listeNiveau3.add(mot);
                break;
            case 4: 
                listeNiveau4.add(mot); 
                break ;
            case 5:
                listeNiveau5.add(mot); 
                break ;
        }
    }
    
    public String getCheminFichierDico(){
        return cheminFichierDico ;
    }
    
    private int vérifieNiveau(int niveau){
        if( niveau > 5 ) {
            niveau = 5 ;
        }
        else if(niveau < 1 ){
            niveau = 1 ;
        }
        return niveau ; 
    }
    
    private String getMotDepuisListe(ArrayList<String> liste){
        return liste.get((int) (Math.random()*(liste.size()-1))) ;
    }
 
    public void lireDictionnaireDOM(String filename, String path){
        try{
            // Analyse le document et crée l’arbre DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDocument = db.parse(filename);
            // Crée un objet "chemin XPath"
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath ();
            NodeList mots = (NodeList) xpath.evaluate("//mot", xmlDocument , XPathConstants.NODESET);
          for(int i = 0; i<mots.getLength();i++){
              Element mot = (Element)mots.item(i);
              String motDico = mot.getTextContent();
              String niveau = mot.getAttribute("niveau");

              ajouteMotADico(Integer.parseInt(niveau),motDico);
          }
        }
        catch(IOException | NumberFormatException | SAXException e){
             System.out.println(e);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Dico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Dico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
