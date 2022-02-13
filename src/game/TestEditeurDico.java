
package game;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class TestEditeurDico{
 Document doc;

public void lireDOM(String fichier){
        // Analyse le document et crée l’arbre DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            try {
                doc = db.parse(fichier);
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
            catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
}

public void ecrireDOM(String fichier){
        try {
            XMLUtil.DocumentTransform.writeDoc(doc, fichier);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

}

//ajouter un mot et son niveau dans le dictionnaire
public void ajouterMot(String mot, int niveau){

    //création d'un Element <mot>
    Element motElement= doc.createElement("mot");
    motElement.appendChild(doc.createTextNode(capitalizeFirstLetter(mot)));
    motElement.setAttribute("niveau", String.valueOf(niveau));
    doc.getDocumentElement().appendChild(motElement);
    
}

public Document getDoc() {
    return doc;
}


//Premiere lettre du mot à ajouter est en majsucule
public static String capitalizeFirstLetter(String value) {
    if (value == null) {
        return null;
    }
    if (value.length() == 0) {
        return value;
    }
    StringBuilder result = new StringBuilder(value);
    result.replace(0, 1, result.substring(0, 1).toUpperCase());
    return result.toString();
}


/*
public static void main(String[] args) {
    TestEditeurDico t = new TestEditeurDico();
    t.lireDOM("TestEditeurDico/dico.xml");
    System.out.print(t.getDoc().getDocumentElement().getTagName());
    t.ajouterMot("ANTICONSTITUTIONNELEMENT", 5);
    t.ajouterMot("FACILE", 1);
    t.ecrireDOM("NewDico.xml");
}
*/
}