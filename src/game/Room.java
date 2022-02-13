/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bulletphysics.collision.dispatch.UnionFind.Element;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ilyss$
 */
public final class Room {
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;

    public Room(){
        setDepth(100);
        setWidth(100);
        setHeight(60);
        setTextureBottom("/textures/skybox/default/bottom.png");
        setTextureEast("textures/skybox/default/east.png");
        setTextureNorth("textures/skybox/default/north.png");
        setTextureWest("textures/skybox/default/west.png");
    }

    public Room(String filename){
        Document _doc = fromXML(filename);
        //Je recupère la racine du doc xml profil.xml
        NodeList racine = (NodeList) _doc.getDocumentElement();
        NodeList dimensions = racine.item(1).getChildNodes();
        height = Integer.parseInt(dimensions.item(1).getTextContent());
        width = Integer.parseInt(dimensions.item(3).getTextContent());
        depth = Integer.parseInt(dimensions.item(5).getTextContent());
        setTextureBottom(_doc.getElementsByTagName("textureBottom").item(0).getTextContent().trim());
        setTextureEast(_doc.getElementsByTagName("textureEast").item(0).getTextContent().trim());
        setTextureWest(_doc.getElementsByTagName("textureWest").item(0).getTextContent().trim());
        setTextureNorth(_doc.getElementsByTagName("textureNorth").item(0).getTextContent().trim());

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

    public int getDepth() {
        return depth;
    }
    public int getHeight() {
        return height;
    }
    public String getTextureBottom() {
        return textureBottom;
    }
    public String getTextureEast() {
        return textureEast;
    }
    public String getTextureNorth() {
        return textureNorth;
    }
    public String getTextureSouth() {
        return textureSouth;
    }
    public String getTextureTop() {
        return textureTop;
    }
    public String getTextureWest() {
        return textureWest;
    }
    public int getWidth() {
        return width;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }
    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }
    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }
    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }
    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }
    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    
}
