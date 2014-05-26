FixturesForJ
============

The aim of the project is to simplify data creation for automated testing.

How to Use:

1. Create a yaml file with the file name as the name of the table.
2. Create records in yaml file, give a heading for the row and the fields represent column name.

   e.g JEEP:
        ID: !!com.fixtures.test.PrimaryKey 1
        NAME: JEEP CASTRO
        TYPE: !!com.fixtures.test.NullableForeignKey ENG_1
        ENGINE_ID: !!com.fixtures.test.NullableForeignKey ENG_2
        
3. 
