<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.portalfiscal.inf.br/nfe"
                xmlns:sig="http://www.w3.org/2000/09/xmldsig#" >
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>

    <xsl:template match="/">
        <xsl:apply-templates select="nfeProc/proc/nfeProc/NFe/infNFe"/>
    </xsl:template>

    <xsl:template match="nfeProc/proc/nfeProc/NFe/infNFe">
{
    "payee": "<xsl:value-of select="emit/xFant"/>",
    "amount": "<xsl:value-of select="total/ICMSTot/vNF"/>",
    "date": "<xsl:value-of select="ide/dhEmi"/>",
    "import_id": "<xsl:value-of select="@Id"/>",
    "items":
    [
        <xsl:apply-templates select="det/prod"/>
    ]
}
    </xsl:template>
    <xsl:template match="det/prod" ><xsl:param name="sep"/>
        { "memo": "<xsl:value-of select="xProd"/>", "total": "<xsl:value-of select="vProd"/>" }<xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>

</xsl:stylesheet>