<?xml version="1.0" encoding="UTF-8"?>
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
                        <xsl:sort select="."/><!-- Ici on tri les mots par ordre alphabétique-->
                    </xsl:apply-templates>

                </table>
            </body>
        </html>
    </xsl:template>
        
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

