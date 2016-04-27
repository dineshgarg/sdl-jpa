* [x] List entities:  
http://localhost:8080/example.svc/Cities  
http://localhost:8080/example.svc/Persons

* [ ] Retrieve by id:  
http://localhost:8080/example.svc/Cities('City-1')  
http://localhost:8080/example.svc/Persons('Person-2')

* [ ] Sub-entities:  
http://localhost:8080/example.svc/Cities('City-1')/persons  
http://localhost:8080/example.svc/Cities('City-1')/persons('Person-1')

* [ ] Values:  
http://localhost:8080/example.svc/Cities('City-1')/name

* [ ] List operations:  
http://localhost:8080/example.svc/Persons?$top=3&$skip=4  
http://localhost:8080/example.svc/Persons/$count  
http://localhost:8080/example.svc/Persons/?$skip=4&$count=true

* [ ] Filter operations:  
http://localhost:8080/example.svc/Persons?$filter=name eq 'my-name'                           
http://localhost:8080/example.svc/Persons?$filter=name eq 'my-name'&$skip=4&$count=true&$top=5
