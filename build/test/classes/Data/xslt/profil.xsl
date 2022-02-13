<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 3 novembre 2021, 15:44
    Author     : ilyss$
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:tux="http://myGame/tux">>
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
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
                    
                    <xsl:for-each select="/tux:profil/tux:parties/tux:partie">
                        <tr>
                            <xsl:variable name="niveau" select="tux:mot/@niveau"/>
                            <xsl:variable name="temps" select="tux:temps"/>
                            <td><xsl:value-of select="@date"/></td>
                            <td><xsl:value-of select="../../tux:avatar" /></td>
                            <td><xsl:value-of select="../../tux:nom" /></td>
                            <xsl:choose>
                                <xsl:when test="$temps &gt; 0">
                                    <td><xsl:value-of select="($niveau * 100) div $temps"/></td>
                                </xsl:when>
                                <xsl:otherwise>
                                    <td>non terminé</td>
                                </xsl:otherwise>
                            </xsl:choose>
                        </tr>
                    </xsl:for-each>

                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
