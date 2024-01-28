Yoann DEWILDE  
Enora DANILO  
M2 ILA - Groupe 1  

# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer

Règle XML :

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

```sh
$ pmd check ./src -R ../../code/Exercise4/MyRuleset.xml
./src/userguide/java/org/apache/commons/math4/userguide/genetics/Polygon.java:97:	ThreeOrMoreNestedIfs:	Three or more nested if blocks
```

**Ici une erreur sera donc détectée :**

```
if (docId != null) {
            final EntityManager em = EntityManagerService.provideEntityManager();
            final Document document = em.find(Document.class, Integer.parseInt(docId));
            if (document != null) {
                if (selectedDocument == null) {
                    selectedDocument = document;
                }
```
