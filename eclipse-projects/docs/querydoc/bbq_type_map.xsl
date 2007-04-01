<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="//sect1[@id='r-types']/refentry">
		<xsl:variable name="id_node" select="@id"/>
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
			<refsect1><title>Relevant Operators</title>
			<xsl:call-template name="add-crossref-section">
				<xsl:with-param name="refnodes"
					select="//sect1[@id='r-setexpressions']//refsect1[title='Type' and .//link[@linkend=$id_node]]"/>
				<xsl:with-param name="title" select="'Producing Set Expressions'"/>
			</xsl:call-template>
			<xsl:call-template name="add-crossref-section">
				<xsl:with-param name="refnodes"
					select="//sect1[@id='r-transforms']//refsect1[title='Results in Type' and .//link[@linkend=$id_node]]"/>
				<xsl:with-param name="title" select="'Producing Transforms'"/>
			</xsl:call-template>
			<xsl:call-template name="add-crossref-section">
				<xsl:with-param name="refnodes"
					select="//sect1[@id='r-value-expressions']//refsect1[title='Results in Type' and .//link[@linkend=$id_node]]"/>
				<xsl:with-param name="title" select="'Producing Value Expressions'"/>
			</xsl:call-template>
			<xsl:call-template name="add-crossref-section">
				<xsl:with-param name="refnodes"
					select="//sect1[@id='r-transforms']//refsect1[title='Applies to Type' and .//link[@linkend=$id_node]]"/>
				<xsl:with-param name="title" select="'Applicable Transforms'"/>
			</xsl:call-template>
			<xsl:call-template name="add-crossref-section">
				<xsl:with-param name="refnodes"
					select="//sect1[@id='r-value-expressions']//refsect1[title='Applies to Type' and .//link[@linkend=$id_node]]"/>
				<xsl:with-param name="title" select="'Applicable Value Expressions'"/>
			</xsl:call-template>
			<xsl:call-template name="add-crossref-section">
				<xsl:with-param name="refnodes"
					select="//sect1[@id='r-filters']//refsect1[title='Applies to Type' and .//link[@linkend=$id_node]]"/>
				<xsl:with-param name="title" select="'Applicable Filters'"/>
			</xsl:call-template>
			</refsect1>
		</xsl:copy>
	</xsl:template>
	<xsl:template name="add-crossref-section">
		<xsl:param name="refnodes"/>
		<xsl:param name="title"/>
		<xsl:if test="$refnodes">
			<refsect2>
				<title><xsl:value-of select="$title"/></title>
				<xsl:for-each select="$refnodes">
					<para>
						<link linkend="{../@id}">
							<xsl:value-of select="../refnamediv/refname"/>
						</link>
					</para>
				</xsl:for-each>
			</refsect2>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
