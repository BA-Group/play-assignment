# Example application using play 2.5 and reactive mongo

# Configure MongoDB

Just change it in application.conf

```
mongodb.uri = "mongodb://localhost/contacts"
```

# Run it

```
sbt run
```

# Data model

The database consists of contact records. A contact has the following form:

```json
{
  _id: String, autogenerated,
  firstName: String, required,
  lastName: String, required,
  email: String, required,
  phone: String, optional,
  city: String, optional
}
```

# Contacts

Template for Contacts controller is provided. Implement the function stubs so that the application provides the following REST-services

## Add some contacts

```
curl -H "Content-Type: application/json" -X POST -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.org}' http://localhost:9000/contacts/
```

## List contacts

```
curl  http://localhost:9000/contacts
```

## Update existing contact

Use a contact id from the list of contacts fetched with the list service

```
curl -H  "Content-Type: application/json" -X PUT -d '{"firstName":"John","lastName":"Doe","email":"doe.john@example.org}' http://localhost:9000/contacts/<contactId>
```

## Get a specific contact

Use a contact id from the list of contacts fetched with the list service

```
curl  http://localhost:9000/contacts/<contactId>
```

## Delete a specific contact

Use a contact id from the list of contacts fetched with the list service

```
curl -X DELETE http://localhost:9000/contacts/<contactId>
```


## Test invalid formats

```
curl -H "Content-Type: application/json" -X POST -d '[{"firstName": "pepe"}]' http://localhost:9000/contacts
```
