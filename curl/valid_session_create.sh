JSON_BODY=$(cat <<EOF
{ 
	"ttl": 3600, 
	"sessionName": "$2",
	"requirements": ["math", "art", "spelling", "technology"]
}
EOF
)

cookie_arg="-b $1"

curl $cookie_arg -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN $1 | awk '{printf $7}')" \
	-d "$JSON_BODY"
echo ""
