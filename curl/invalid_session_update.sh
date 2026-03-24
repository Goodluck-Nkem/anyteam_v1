DATA=$(cat <<EOF
{ 
	"ttl": null, 
	"sessionId": "$1"
}
EOF
)

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/session/update \
	-H "Content-Type: application/json" \
	-d "$DATA"
echo ""
