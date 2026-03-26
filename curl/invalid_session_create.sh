DATA_REQ_OUT_OF_RANGE=$(cat <<EOF
{ 
	"ttl": 3600, 
	"sessionName": "INVALID",
	"requirements": ["math", "art", "spelling", "math", "art", "spelling"]
}
EOF
)

DATA_TTL_NAN=$(cat <<EOF
{ 
	"ttl": "ABC", 
	"sessionName": "INVALID",
	"requirements": ["math", "art", "spelling", "math", "art", "spelling", "logic"]
}
EOF
)

DATA_REQ_FAKE=$(cat <<EOF
{ 
	"ttl": 300, 
	"sessionName": "INVALID",
	"requirements": [ "math", "art", "spelling", "fake_req" ]
}
EOF
)


DATA_REQ_NULL_ELEMENT=$(cat <<EOF
{ 
	"ttl": 300, 
	"sessionName": "INVALID",
	"requirements": ["language", "art", "spelling", "ART", null]
}
EOF
)

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
	-d "$DATA_REQ_OUT_OF_RANGE"
echo ""

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
	-d "$DATA_TTL_NAN"
echo ""

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
	-d "$DATA_REQ_FAKE"
echo ""

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: application/json" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
	-d "$DATA_REQ_NULL_ELEMENT"
echo ""

curl -b cookie/admin.txt -D - -X POST http://localhost:8080/api/v1/session/create \
	-H "Content-Type: text/plain" \
	-H "X-XSRF-TOKEN: $(grep XSRF-TOKEN cookie/admin.txt | awk '{printf $7}')" \
	-d "$DATA_REQ_FAKE"
echo ""

