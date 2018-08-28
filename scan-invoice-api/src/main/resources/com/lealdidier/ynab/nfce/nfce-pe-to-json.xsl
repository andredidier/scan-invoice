<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:n="http://www.portalfiscal.inf.br/nfe">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>

    <xsl:template match="/">
        <xsl:apply-templates select="n:nfeProc/n:proc/n:nfeProc/n:NFe/n:infNFe"/>
    </xsl:template>

    <xsl:template match="n:nfeProc/n:proc/n:nfeProc/n:NFe/n:infNFe">
{
    "payee": "<xsl:value-of select="n:emit/n:xFant"/>",
    "amount": "<xsl:value-of select="n:total/n:ICMSTot/n:vNF"/>",
    "date": "<xsl:value-of select="n:ide/n:dhEmi"/>",
    "import_id": "<xsl:value-of select="@Id"/>",
    "items":
    [
        <xsl:apply-templates select="n:det/n:prod"/>
    ]
}
    </xsl:template>
    <xsl:template match="n:det/n:prod" >
        { "memo": "<xsl:value-of select="n:xProd"/>", "total": "<xsl:value-of select="n:vProd"/>" },
    </xsl:template>

</xsl:stylesheet>