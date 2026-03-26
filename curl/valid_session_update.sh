DATA=$(cat <<EOF
{ 
	"ttl": -1, 
	"sessionId": "$2"
}
EOF
)

cookie_arg="-b $1"

curl $cookie_arg -X POST http://localhost:8080/api/v1/session/update \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN $1 | awk '{printf $7}')" \
	-d "$DATA"
echo ""
