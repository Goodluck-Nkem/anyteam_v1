curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
  -d '{ 
	  	"ttl": null 
	}'
echo ""

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
  -d '{}'
echo ""

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
  -d '{ 
	  	"ttl": -1
		_-
	}'
echo ""


