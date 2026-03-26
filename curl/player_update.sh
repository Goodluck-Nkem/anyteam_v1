DATA=$(cat <<EOF
{  
	"firstName": "$2",
	"lastName": "$3"
}
EOF
)

if [ $# -eq 0 ]; then
	echo "Cookie file not provided!"
	exit 0
fi

curl -D - -b $1 -X POST http://localhost:8080/api/v1/player/update \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN $1 | awk '{printf $7}')" \
	-d "$DATA"
echo ""

