FixturesForJ
============

The aim of the project is to simplify data creation for automated testing.

####Dependency:

`Spring-jdbc` module.

####Usage:

1. Create a yaml file with the file name as the name of the table.
2. Create records in yaml file, give a heading for the row and the fields represent column name.

   e.g 
   ````
   t_automobile.yml
   JEEP:
        ID: !!com.fixtures.data.structure.PrimaryKey 1
        NAME: JEEP CASTRO
        TYPE: !!com.fixtures.data.structure.NullableForeignKey ENG_1
        ENGINE_ID: !!com.fixtures.data.structure.NullableForeignKey ENG_2
   ````     
3. Just represent foreign keys with the section name and api automatically detects its corresponding table (yaml files) and the section.

   e.g

   For section ````JEEP````, the column ````TYPE```` will be inserted with the primary key of the section ````ENG_1```` from table ```t_engine```

   ````
   t_engine.yml
   ENG_1:
     ID: !!com.fixtures.data.structure.PrimaryKey 1
     TYPE: V
   ````  
4. Wire `FixturesForJ` in your `test`, all it needs is instance of `jdbcTemplate`.

   
####Development:
   The project is still in development phase.
   
####Test:
   
   1. Before running the ```test```, download ```hsqldb```, ```apache maven``` and start the database.
   2. run ```mvn test -Pdb-refresh```
