## Add your notes here

* HATEOAS based paging implemented. 
* Tax configuration implemented. Reading method. Editing configuration  is out of scope.
* WebMvcTest provided for controllers
* Integration tests are not implemented.
* Dao layer needed to be improved (validation, datasource configuration, DDL for tables) 
* Logs need to be provided.
* Dependencies added:
  - mapstruct - to convert entities to dto
  - lombok-mapstruct-binding - to make mapstruct and lombok be friends
  - hibernate version incised to 6.3.1.Final ( I run into a bug which was fixed in 6.3 version )
