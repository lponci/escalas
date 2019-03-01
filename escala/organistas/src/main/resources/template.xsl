<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="Escala">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-landscape" page-height="21.0cm" page-width="29.7cm" margin="2cm"
                                       margin-left="4.8cm" margin-top="4.1cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-landscape">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block-container height="8mm" width="32%">
                        <fo:block>
                            <fo:inline padding-left="25mm" font-style="italic" font-size="9pt">
                                <xsl:value-of select="@nome"/>
                            </fo:inline>
                        </fo:block>
                    </fo:block-container>
                    <fo:table table-layout="fixed" width="35%" font-size="8pt" text-align="center"
                              display-align="center">
                        <fo:table-column column-width="50%"/>
                        <fo:table-column column-width="4%"/>
                        <fo:table-column column-width="50%"/>
                        <fo:table-body>
                            <xsl:for-each select="Mes[position() mod 2 = 1]">
                                <fo:table-row>
                                    <fo:table-cell>
                                        <xsl:call-template name="tabela">
                                            <xsl:with-param name="contexto" select="."/>
                                        </xsl:call-template>
                                        <fo:block space-before="2mm" />
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block space-before="1mm" />
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <xsl:call-template name="tabela">
                                            <xsl:with-param name="contexto" select="following-sibling::*[1]"/>
                                        </xsl:call-template>
                                        <fo:block space-before="2mm" />
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>
                    <fo:block space-before="10mm" />

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

   <xsl:template name="getNode">
        <xsl:param name="contexto" />
        <fo:block>
            <xsl:value-of select="$contexto[@nome]" />
        </fo:block>
    </xsl:template>

    <xsl:template name="tabela">
        <xsl:param name="contexto"/>
        <fo:table table-layout="fixed" width="100%" font-size="8pt" border-color="black" border-width="0.2mm"
                  border-style="solid">
            <fo:table-column column-width="5mm"/>
            <fo:table-column column-width="5mm"/>
            <fo:table-column column-width="5mm"/>
            <fo:table-column column-width="proportional-column-width(35)"/>
            <fo:table-header>
                <fo:table-cell number-columns-spanned="3">
                    <fo:block background-color="lightgray" border-width="0.1mm" border-style="solid" font-weight="bold">
                        Data
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell>
                    <fo:block background-color="lightgray" border-width="0.1mm" border-style="solid" font-weight="bold">
                        Irmãs
                    </fo:block>
                </fo:table-cell>
            </fo:table-header>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block-container reference-orientation="90">
                            <fo:block wrap-option="no-wrap">
                                <xsl:value-of select="$contexto/nomeMes"/>
                            </fo:block>
                        </fo:block-container>
                    </fo:table-cell>
                    <fo:table-cell>
                        <xsl:for-each select="$contexto/Dias/diaToEscalada/item">
                            <xsl:variable name="dia" select="substring-before(@id, '-')" />
                            <xsl:variable name="color">
                                <xsl:choose>
                                    <xsl:when test="substring-after(@id, '-') = 'D'">
                                        <xsl:value-of select="'#FF0000'" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="'#000000'" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:variable>
                            <xsl:call-template name="tabelaRow">
                                <xsl:with-param name="cor" select="$color" />
                                <xsl:with-param name="empty"/>
                                <xsl:with-param name="atributo" select="$dia"/>
                            </xsl:call-template>
                            <fo:block>
                                <xsl:if test="position() = 4 and last() = 4">
                                    <xsl:call-template name="tabelaRow">
                                        <xsl:with-param name="cor" select="$color" />
                                        <xsl:with-param name="empty">-</xsl:with-param>
                                        <xsl:with-param name="atributo" select="."/>
                                    </xsl:call-template>
                                </xsl:if>
                            </fo:block>
                        </xsl:for-each>
                    </fo:table-cell>
                    <fo:table-cell>
                        <xsl:for-each select="$contexto/Dias/diaToEscalada/item">
                            <xsl:variable name="diaSemana" select="substring-after(@id, '-')" />
                            <xsl:variable name="color">
                                <xsl:choose>
                                    <xsl:when test="$diaSemana = 'D'">
                                        <xsl:value-of select="'#FF0000'" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="'#000000'" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:variable>
                            <xsl:call-template name="tabelaRow">
                                <xsl:with-param name="cor" select="$color" />
                                <xsl:with-param name="empty"/>
                                <xsl:with-param name="atributo" select="concat($diaSemana, substring('ª', 1, 7 * not($diaSemana = 'D')))"/>
                            </xsl:call-template>
                            <fo:block>
                                <xsl:if test="position() = 4 and last() = 4">
                                    <xsl:call-template name="tabelaRow">
                                        <xsl:with-param name="cor" select="$color" />
                                        <xsl:with-param name="empty">-</xsl:with-param>
                                        <xsl:with-param name="atributo" select="."/>
                                    </xsl:call-template>
                                </xsl:if>
                            </fo:block>
                        </xsl:for-each>
                    </fo:table-cell>
                    <fo:table-cell>
                        <xsl:for-each select="$contexto/Dias/diaToEscalada/item">
                            <xsl:call-template name="tabelaRow">
                                <xsl:with-param name="cor">#000000</xsl:with-param>
                                <xsl:with-param name="empty"/>
                                <xsl:with-param name="atributo" select="@nome"/>
                            </xsl:call-template>
                            <fo:block>
                                <xsl:if test="position() = 4 and last() = 4">
                                    <xsl:call-template name="tabelaRow">
                                        <xsl:with-param name="cor">#000000</xsl:with-param>
                                        <xsl:with-param name="empty">-</xsl:with-param>
                                        <xsl:with-param name="atributo" select="." />
                                    </xsl:call-template>
                                </xsl:if>
                            </fo:block>
                        </xsl:for-each>
                        <fo:block space-after="1mm"/>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <xsl:template name="tabelaRow">
        <xsl:param name="cor"/>
        <xsl:param name="empty"/>
        <xsl:param name="atributo"/>
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="100%"/>
            <fo:table-body border-color="black" border-width="0.1mm" border-style="solid">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>
                            <fo:inline color="{$cor}">
                                <xsl:choose>
                                    <xsl:when test="$empty = '-'">
                                        <xsl:value-of select="$empty"/>
                                    </xsl:when>
                                    <xsl:when test="contains($atributo, '=')">
                                        <xsl:value-of select="substring-before($atributo, ' =')"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="$atributo"/>
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