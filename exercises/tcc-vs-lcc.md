# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Graphe:
- sommet: méthode
- arc: attribut en commun

```
TCC: nombre de paires directement connectées (= nombre d'arcs) / nombre de paires dans le graphe
LCC: nombre de paires connectées / nombre de paires dans le graphe
```

### TCC = LCC

Ils sont égaux quand il n'y a que des paires directement connectées, quand le graphe est complet. C'est à dire que chaque méthode a au moins un attribut en commun avec toutes les autres méthodes.

```java
class Point {

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double dot(Point p) {
        return x*p.x + y*p.y;
    }
}
```

### LCC < TCC

Il faut : `nombre de paires connectées < nombre de paires directement connectées`. Ce qui est impossible, donc LCC ne peut être inférieur à TCC.