TTL="$1"
if [ "$#" -eq 0 ]; then
	TTL=0
fi

DATA_TTL=$(cat <<EOF
{ 
	"ttl": $TTL
}
EOF
)

curl -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -d "$DATA_TTL" 
echo ""
