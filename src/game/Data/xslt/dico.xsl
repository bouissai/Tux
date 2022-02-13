<?xml version="1.0" encoding="UTF-8"?>
<!--
    Document   : dico.xsl
    Author     : BOUISSA ilyass / CRIVOI Dmitrii
    Description: Transformation XSL afin d'afficher en html les 
    mots ansi que leur niveau du dico.xml dans l'ordre Alphabetique
        Purpose of transformation follows.
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tux="http://myGame/tux">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <html>
            <body>
                <h2>Dictionnaire</h2>
                <table border="1">
                    <tr bgcolor="#9acd32">
                        <th>Mot</th>
                        <th>Niveau</th>
                    </tr>
                    <!-- Pour chaque mot on affiche le mot ainsi que son niveau-->
                    <xsl:apply-templates select="//tux:mot">
                        <!-- Ici on tri les mots par ordre alphabétique-->
                        <xsl:sort select="."/>
                    </xsl:apply-templates>

                </table>
            </body>
        </html>
    </xsl:template>
    
    <!-- 
    Pour chaque mot on affiche dans la premiere colonne le mot en lui même
    Et dans la 2e colonne le niiveau qui correspond
    -->
    <xsl:template match="tux:mot">
        <tr>
            <td>
                <xsl:value-of select="." />
            </td>
            <td>
                <xsl:value-of select="@niveau" />
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>

