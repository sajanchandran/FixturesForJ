FixturesForJ
============

The aim of the project is to simplify data creation for automated testing.

##Usage:

1. Create a yaml file with the file name as the name of the table.
2. Create records in yaml file, give a heading for the row and the fields represent column name.

   e.g 
   ````
   t_automobile.yml
   JEEP:
        ID: !!com.fixtures.test.PrimaryKey 1
        NAME: JEEP CASTRO
        TYPE: !!com.fixtures.test.NullableForeignKey ENG_1
        ENGINE_ID: !!com.fixtures.test.NullableForeignKey ENG_2
   ````     
3. Just represent foreign keys with the section name and api automatically detects its corresponding table (yaml files) and the section.

   e.g

   For section ````JEEP````, the column ````TYPE```` will be inserted with the primary key of the section ````ENG_1```` from table ```t_engine```

   ````
   t_engine.yml
   ENG_1:
     ID: !!com.fixtures.test.PrimaryKey 1
     TYPE: V
   ````  
   
##Development:
   The project is still in development phase, currently it doesnt support insertion of ```date```
