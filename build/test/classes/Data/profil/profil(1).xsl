<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 3 novembre 2021, 15:44
    Author     : ilyss$
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:tux="http://myGame/tux">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
                <link rel="stylesheet" href="profil.css"/>
                <title>profil.xsl</title>
            </head>
            <body>
                <table border="1">
                    <tr>
                        <th>Parties</th>
                        <th>Avatar</th>
                        <th>Identité</th>
                        <th>Scores</th>
                    </tr>
                    <!-- 
                        Pour chaque partie on va remplir les lignes du tableau à 3 colonnes: 
                            - Parties 
                            - Avatar
                            - Identité
                            - Scores
                    -->            
                    <xsl:apply-templates select ="/tux:profil/tux:parties/tux:partie"/>
                </table>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:partie">
        <tr>
            <!-- on a une variable qui stocke le niveau du mot -->
            <xsl:variable name="niveau" select="tux:mot/@niveau"/>
            <!-- on a une variable qui stocke le temps du mot -->
            <xsl:variable name="temps" select="tux:temps"/>
            <!-- Dans la 1er colonne (Parties) on affiche la date de la partie -->
            <td><xsl:value-of select="@date"/></td>
            <!-- Dans la 2e colonne (Avatar) on affiche le lien l'image pour avatar  -->
            <td><xsl:value-of select="../../tux:avatar" /></td>
            <!-- Dans la 3e colonne (Identité) on affiche le nom des joueurs  -->
            <td><xsl:value-of select="../../tux:nom" /></td>
            <!-- Dans la 4e colonne (Scores) on affiche le scores des joueurs  -->
            <xsl:choose>
                <!-- Pour calculer le score si on a un temps (le temps est forcement >0)  -->
                <xsl:when test="$temps &gt; 0">
                    <td><xsl:value-of select="($niveau * 100) div $temps"/></td>
                </xsl:when>
                <!-- Si on pas de temps on renvoit rien pour le score -->
                <xsl:otherwise>
                    <td></td>
                </xsl:otherwise>
            </xsl:choose>
        </tr>
    </xsl:template>

</xsl:stylesheet>
