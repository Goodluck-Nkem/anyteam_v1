DATA_TTL=$(cat <<EOF
{ 
	"ttl": 3600 
}
EOF
)

curl -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -d "$DATA_TTL" 
echo ""
