package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.bulletphysics.collision.broadphase.Dbvt.Node;
import com.jme3.terrain.heightmap.HillHeightMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/*
créer un fichier de hi-scores dès le premier lancement du programme, d'actualiser une liste (au début vide) des meilleures parties, 
et de toujours tester si une partie nouvellement jouée rentre dans le classement comme une des 10 meilleures parties ou non.
*/
public class Scores {
    Document _doc;

    //Méthode pour initaliser la liste trié à partir d'un XML
    public ArrayList<Integer> liste_score(String nomFichier){
            // Crée un objet "chemin XPath"
            ArrayList<Integer> highScores = new ArrayList<Integer>();
            _doc = fromXML(nomFichier);
        // Analyse le document et crée l’arbre DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document xmlDocument = db.parse(nomFichier);
            // Crée un objet "chemin XPath"
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath ();
            try {
                NodeList scores = (NodeList) xpath.evaluate("//score", xmlDocument , XPathConstants.NODESET);
                for(int i = 0; i<scores.getLength();i++){
                    Element score = (Element)scores.item(i);
                    highScores.add(Integer.parseInt(score.getTextContent())); //J'ajoute les different score de mon xml a ma liste
                }
                Collections.sort(highScores);
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            } catch (ParserConfigurationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SAXException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            _doc = fromXML(nomFichier);
        return highScores;
    }
    //Renvoie la liste modifié si le paramètre fais partie du TOP 10
    public ArrayList<Integer> HighScore(int score,  ArrayList<Integer> highScores){
        for(int k=0;k<highScores.size();k++){
            if(score> highScores.get(k)){
                highScores.add(k,score); //On insère le score 
                if(highScores.size()>10){ //On supprime le dernier element de la liste ssi il ne fais plsu parti du top10
                    Collections.sort(highScores,Collections.reverseOrder());//on trie notre liste 
                    highScores.remove(highScores.size()-1);
                }
                Collections.sort(highScores,Collections.reverseOrder());//on trie notre liste 
                return highScores;
            }
            Collections.sort(highScores,Collections.reverseOrder());//on trie notre liste 
        }
        highScores = new ArrayList<Integer>();
        highScores.add(score);
        return highScores;

    }


    
    // Cree un DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

     // sauvegarder le document DOM dans un fichier XML.
     void sauvegarder_score(String filename, ArrayList<Integer> highScores){
        //TODO on converti la date de la partie
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                _doc = dBuilder.newDocument();
                //Element racine <scores>
                Element racineScores = _doc.createElement("scores");
                _doc.appendChild(racineScores);
                for(int i=0;i<highScores.size();i++){
                    //Element fils <score>
                    Element score = _doc.createElement("score");
                    score.appendChild(_doc.createTextNode(String.valueOf(highScores.get(i))));
                    racineScores.appendChild(score);
                }
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        toXML(filename);
    }

    

    // Sauvegarde un DOM en XML
    public void toXML(String nomFichier ) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void main(String[] args) { 
        Scores scores = new Scores();
        ArrayList<Integer> highScores = new ArrayList<Integer>();
        highScores = scores.liste_score("src/game/Data/xml/hi-scores.xml");//Je lis l'xml
        //highScores = scores.HighScore(15, highScores);     
        scores.sauvegarder_score("src/game/Data/xml/hi-scores.xml", highScores);
        }

}
