curl -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -d '{ 
	  	"ttl": null 
	}'
echo ""

curl -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -d '{}'
echo ""

curl -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -d '{ 
	  	"ttl": -1
		_-
	}'
echo ""


