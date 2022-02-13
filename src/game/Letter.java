/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.advanced.EnvNode;
/**
 *
 * @author ilyass
 */
public class Letter extends EnvNode{
    private char letter;
    public Letter(char l, double x, double y){
        //Dans le constructeur
        this.letter = l;
        setScale(4);
        setX(x);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille du cube
        setZ(y); // positionnement en y
        if(letter == ' '){
            setTexture("models/letter/cube.png");
        }
        else{
            String s = "models/letter/"+(letter+"").toLowerCase()+".png";
            setTexture(s);
        }
        setModel("models/letter/cube.obj");
    }
}
