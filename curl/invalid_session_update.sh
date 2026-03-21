DATA=$(cat <<EOF
{ 
	"ttl": null, 
	"sessionId": "$1"
}
EOF
)

curl -D - -X POST http://localhost:8080/api/v1/session/update \
	-H "Content-Type: application/json" \
	-d "$DATA"
echo ""
