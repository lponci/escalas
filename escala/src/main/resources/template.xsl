<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="Escala">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-landscape" page-height="21.0cm" page-width="29.7cm" margin="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-landscape">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block >
                        <xsl:value-of select="@nome" />
                    </fo:block>
                    <xsl:for-each select="Mes">
                        <fo:block>
                            <xsl:value-of select="position() mod 2" />
                        </fo:block>
                        <!--border-color="black" border-width="0.2mm" border-style="solid" -->
                        <fo:table table-layout="fixed" width="100%" space-before="2mm" font-size="8pt" border-color="black" border-width="0.2mm" border-style="solid">
                            <fo:table-column column-width="5mm"/>
                            <fo:table-column column-width="6mm"/>
                            <fo:table-column column-width="18mm"/>
                            <fo:table-header text-align="center" display-align="center">
                                <fo:table-cell number-columns-spanned="2">
                                    <fo:block border-width="0.1mm" border-style="solid" font-weight="bold">Data</fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block border-width="0.1mm" border-style="solid" font-weight="bold">Irmãs</fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell text-align="center" display-align="center">
                                        <fo:block-container reference-orientation="90">
                                            <fo:block  wrap-option="no-wrap">
                                                <xsl:value-of select="nomeMes" />
                                            </fo:block>
                                        </fo:block-container>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <xsl:for-each select="Dias/dia/string">
                                            <xsl:call-template name="tabelaRow">
                                                <xsl:with-param name="cor">#FF0000</xsl:with-param>
                                                <xsl:with-param name="empty" />
                                            </xsl:call-template>
                                            <fo:block>
                                                <xsl:if test="position() = 4 and last() = 4">
                                                    <xsl:call-template name="tabelaRow">
                                                        <xsl:with-param name="cor">#FF0000</xsl:with-param>
                                                        <xsl:with-param name="empty">-</xsl:with-param>
                                                    </xsl:call-template>
                                                </xsl:if>
                                            </fo:block>
                                        </xsl:for-each>
                                        <!--<fo:block space-before="1mm" />-->
                                    </fo:table-cell>
                                    <fo:table-cell text-align="center" display-align="center">
                                        <xsl:for-each select="Dias/escalada/string">
                                            <xsl:call-template name="tabelaRow">
                                                <xsl:with-param name="cor">#000000</xsl:with-param>
                                                <xsl:with-param name="empty" />
                                            </xsl:call-template>
                                            <fo:block>
                                                <xsl:if test="position() = 4 and last() = 4">
                                                    <xsl:call-template name="tabelaRow">
                                                        <xsl:with-param name="cor">#000000</xsl:with-param>
                                                        <xsl:with-param name="empty">-</xsl:with-param>
                                                    </xsl:call-template>
                                                </xsl:if>
                                            </fo:block>
                                        </xsl:for-each>
                                        <fo:block space-after="1mm"/>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </xsl:for-each>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template name="tabelaRow">
        <xsl:param name="cor"/>
        <xsl:param name="empty"/>
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="100%"/>
            <fo:table-body border-color="black" border-width="0.1mm" border-style="solid">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block display-align="center" text-align="center">
                            <fo:inline color="{$cor}">
                                <xsl:choose>
                                    <xsl:when test="$empty = '-'">
                                        <xsl:value-of select="$empty" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="." />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
</xsl:stylesheet>