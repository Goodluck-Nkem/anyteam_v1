TTL="$2"
if [ "$#" -le 1 ]; then
	TTL=0
fi

DATA_TTL=$(cat <<EOF
{ 
	"ttl": $TTL
}
EOF
)

cookie_arg="-b $1"

curl $cookie_arg -X POST http://localhost:8080/api/v1/sysconfig \
  -H "Content-Type: application/json" \
  -H "X-XSRF-TOKEN: $(grep XSRF-TOKEN $1 | awk '{printf $7}')" \
  -d "$DATA_TTL" 
echo ""
