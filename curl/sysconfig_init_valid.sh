curl -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -d "{ 
	  	\"ttl\": $1 
	}"
echo ""
