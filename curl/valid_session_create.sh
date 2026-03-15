JSON_BODY=$(cat <<EOF
{ 
	"ttl": 3600, 
	"sessionName": "$1",
	"requirements": ["math", "art", "spelling", "technology"]
}
EOF
)

curl -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: application/json" \
	-d "$JSON_BODY"
echo ""
