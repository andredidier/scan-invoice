<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:nfe="http://www.portalfiscal.inf.br/nfe"
                xmlns:sig="http://www.w3.org/2000/09/xmldsig#" >
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>

    <xsl:template match="/">
        <xsl:apply-templates select="nfe:nfeProc/nfe:proc/nfe:nfeProc/nfe:NFe/nfe:infNFe"/>
    </xsl:template>

    <xsl:template match="nfe:nfeProc/nfe:proc/nfe:nfeProc/nfe:NFe/nfe:infNFe">
{
    "payee": "<xsl:value-of select="nfe:emit/nfe:xFant"/>",
    "amount": "<xsl:value-of select="nfe:total/nfe:ICMSTot/nfe:vNF"/>",
    "date": "<xsl:value-of select="nfe:ide/nfe:dhEmi"/>",
    "import_id": "<xsl:value-of select="@Id"/>",
    "items":
    [
        <xsl:apply-templates select="nfe:det/nfe:prod"/>
    ]
}
    </xsl:template>
    <xsl:template match="nfe:det/nfe:prod" ><xsl:param name="sep"/>
        { "memo": "<xsl:value-of select="nfe:xProd"/>", "total": "<xsl:value-of select="nfe:vProd"/>" }<xsl:if test="position() != last()">,</xsl:if>
    </xsl:template>

</xsl:stylesheet>