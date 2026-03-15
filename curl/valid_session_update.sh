DATA=$(cat <<EOF
{ 
	"ttl": -1, 
	"sessionId": "$1"
}
EOF
)

curl -X POST http://localhost:8080/api/v1/session/update \
	-H "Content-Type: application/json" \
	-d "$DATA"
echo ""
