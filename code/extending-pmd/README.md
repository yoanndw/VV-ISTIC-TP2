# Code of your exercise

Put here all the code created for this exercise

Règle qui vérifie qu'il y a moins de 3 niveaux d'imbrication de `if` :

```xml
<rule name="ThreeOrMoreNestedIfs"
      language="java"
      message="Three or more nested if blocks"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
  <description>
    More than 2 if blocks are nested
  </description>
  <priority>3</priority>

  <properties>
    <property name="version" value="3.1"/>
    <property name="xpath">
      <value>
<![CDATA[
//IfStatement//IfStatement//IfStatement
 ]]>
      </value>
    </property>
  </properties>
</rule>
```