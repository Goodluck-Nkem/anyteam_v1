curl -X POST http://localhost:8080/api/v1/session/create \
  -H "Content-Type: application/json" \
  -d '{ 
	  	"ttl": 3600, 
		"sessionName": "Fury",
		"requirements": ["math", "art", "spelling", "technology"]
	}'
echo ""
