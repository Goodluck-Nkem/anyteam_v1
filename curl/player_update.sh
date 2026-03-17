DATA=$(cat <<EOF
{ 
	"playerId": "$1", 
	"firstName": "$2",
	"lastName": "$3"
}
EOF
)

curl -X POST http://localhost:8080/api/v1/player/update \
	-H "Content-Type: application/json" \
	-d "$DATA"
echo ""

