[![Build Status](https://travis-ci.org/nerdle/nerdle.svg?branch=master)](https://travis-ci.org/nerdle/nerdle)

Nerdle
======

Topic-Specific Question Answering Using Wikia Seeds

## Summary

The WIKIA project maintains wikis across a diverse range of subjects from areas of popular culture. Each wiki consists of collaboratively authored content and focuses on a particular topic, including franchises such as “Star Trek”, “Star Wars” and “The Simpsons”. Nerdle is a topic-expert Question Answering system which can answer questions on a particular topic and supports questions of the types “Who”, “What”, “Where”, “Why”, “When”, “Which”, “Whom” and “How?”.

## Get Started

### Prerequisites

* Apache Maven 3
* Java >= 1.7

### Build from source
```
git clone https://github.com/nerdle/nerdle.git
cd nerdle
mvn clean package
```
_Nerdle_ is now installed in `nerdle/target`

### Maven dependency
```shell
git clone https://github.com/nerdle/nerdle.git
cd nerdle
mvn clean install
```
_Nerdle_ is now installed in your local maven repository.

```xml
<dependency>
   <groupId>de.tu_berlin.dima</groupId>
   <artifactId>nerdle-core</artifactId>
   <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Sample Usage

Use a question answering system to answer natural language questions. Lets take a simple example for how to use the system.

Using `NerdleAnswerer` and a `FactProvider` you can now pose natural language questions to the system. The system can run on two different `FactProviders`. You can use either a rational database (`DBFactProvider`) over jdbc or the in-memory graph database `Tinkerpop`.

* DBFactProvider

```java
public class MyNerdle {

    public static void main(String[] args) throws Exception {
        String topic = "nerdle_test";
        DBSingleton dbSingleton = new DBSingleton();
        DBConnection dbConnection = dbSingleton.getConnections().get(topic);
        FactProvider factProvider = new DBFactProvider(dbConnection);

        NerdleAnswerer nerdleAnswerer = new NerdleAnswerer();
        nerdleAnswerer.getAnswers(factProvider, "Who is funny?", 1);
    }

}
```

* TinkerpopFactProvider

```java
public class MyNerdle {

    public static void main(String[] args) throws Exception {
        String topic = "nerdle_test";
        Graph graph = TinkerpopGraphSingleton.getInstance().getGraphs().get(topic);
        FactProvider factProvider = new TinkerpopFactProvider(graph);
        NerdleAnswerer nerdleAnswerer = new NerdleAnswerer();
        nerdleAnswerer.getAnswers(factProvider, "Who is funny?", 1);

        TinkerpopGraphSingleton.getInstance().shutdown();
    }

}
```

For each _topic_ you have to provide a `FactProvider` using the `nerdle_config.properties` file. To create the file use the `nerdle_config_template.properties`. Put this file in `src/main/resources/nerdle_config.properties`.

```data
# TINKERPOP, DB
factprovider=DB

# TINKERPOP properties
tinkerpop.basePath=/path/to/graphs
tinkerpop.graphs=simpsons
tinkerpop.graphs=star-trek
tinkerpop.graphs=wookieepedia

# DB properties
db.base_url=jdbc:postgresql://localhost/
db.user=user_name
db.password=password
db.databases=nerdle_simpsons
db.databases=nerdle_star-trek
db.databases=nerdle_star-wars
```

For junits tests the `src/test/resources/nerdle_test_config.properties` file is used.

## Citing Nerdle

If you use Nerdle in your academic work, please cite Nerdle with the following BibTeX citation:

```latex
@InProceedings{maqsud-EtAl:2014:ColingDemo,
  author    = {Maqsud, Umar  and  Arnold, Sebastian  and  H\"{u}lfenhaus, Michael  and  Akbik, Alan},
  title     = {Nerdle: Topic-Specific Question Answering Using Wikia Seeds},
  booktitle = {Proceedings of COLING 2014, the 25th International Conference on Computational Linguistics: System Demonstrations},
  month     = {August},
  year      = {2014},
  address   = {Dublin, Ireland},
  publisher = {Dublin City University and Association for Computational Linguistics},
  pages     = {81--85},
  url       = {http://www.aclweb.org/anthology/C14-2018}
}
```

## License

_Nerdle_ is licensed under the Apache Software License Version 2.0. For more
information please consult the LICENSE file.
