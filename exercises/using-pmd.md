Yoann DEWILDE  
Enora DANILO  
M2 ILA - Groupe 1  

# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

Vrai positif (devrait être résolu) :

```sh
$ pmd check ./src -R category/java/design.xml/SimplifyBooleanReturns
./src/userguide/java/org/apache/commons/math4/userguide/genetics/HelloWorldExample.java:76:	SimplifyBooleanReturns:	This if statement can be replaced by `return {condition};`
```

Faux positif (devrait être ignoré) :

```sh
$ pmd check ./commons-math-core/ -R category/java/codestyle.xml/UnnecessaryLocalBeforeReturn
./commons-math-core/src/main/java/org/apache/commons/math4/core/jdkmath/AccurateMath.java:1566:	UnnecessaryLocalBeforeReturn:	Consider simply returning the value vs storing it in local variable 'result'
```
